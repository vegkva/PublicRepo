import csv
import os
import sys
import json
import argparse
from _csv import writer
from socket import create_server, timeout, socket, AF_INET, SOCK_DGRAM
from threading import Thread
from databaseClient import DatabaseClient
from dotenv import load_dotenv

class StorageServer:
    """Class for server handling connections between database, FMI, and weather stations.

    Parameters
    ----------
    host
        Host to run server on.
    port
        Port to run server on.
    main
        Default value False. Set to True if not imported as a script.
    """

    def __init__(self, host, port, main=False, interactive=False):
        self._main = main

        # Instantiate database
        self._database = DatabaseClient(username="DB_Client", password=os.getenv("password"),
                                        clusterName="INF142-cluster-test", database="weatherstation")

        # Instantiate and bind a TCP socket.
        self._station_sock = create_server((host, port))
        self._station_sock.settimeout(5)
        # Instantiate and bind a UDP socket.
        self._fmi_sock = socket(AF_INET, SOCK_DGRAM)
        self._fmi_sock.bind((host, port))
        self._fmi_sock.settimeout(5)
        self._BUFFER_SIZE = 1024
        self._data = ""
        self._delete_station = ""
        # If we want interactive mode
        self._interactive = interactive

    def turn_on(self):
        """Turns on the server.

        Initializes accepting incoming TCP and UDP connections.
        """
        self._serving = True
        Thread(target=self._tcp_accept).start()
        Thread(target=self._udp_accept).start()
        print("[Server running]")

        if self._main and self._interactive:
            self._await_command()

    def _await_command(self):
        """Commands for the server.

        Continuously listens for a command and ENTER keypress.
        """
        while self._serving:
            command = input("> ")
            if command == "turn off" or command == "shut down":
                self.turn_off()
            if command == "delete":
                del_station = input("What collection do you want to delete (city)?")
                while input(f"Are you sure you want to delete collection:{del_station}? (y/n)") == "y":
                    self._database.clear_collection(station=del_station, are_you_sure=True)
                    break

    def turn_off(self):
        """Shuts the server down.

        Also closes the accepting threads.
        """
        self._serving = False
        print("[Shutting down]")

    def _udp_accept(self):
        """Accepts UDP connections from FMI/Web Server to the server.

        Only supports GET requests.
        Responds with an encoded json string.
        """
        while self._serving:
            try:
                data, addr = self._fmi_sock.recvfrom(self._BUFFER_SIZE)
                data = data.decode().split(" ")
                print(f"[Data requested from {addr}, {data}]")

                if len(data) < 1 or data[0] != "GET":
                    self._fmi_sock.sendto(b"Invalid request", addr)
                    continue

                # If client is requesting a specific city, then get it, else get all
                if len(data) > 1:
                    stations = [data[1]]
                else:
                    stations = self._database._database.list_collection_names()

                _data = []
                for station in stations:
                    _data += self._database.retrieve_data(station)

                self._fmi_sock.sendto(json.dumps(_data).encode(), addr)
            except:
                pass

    def _tcp_accept(self):
        """Accepts TCP connections to the server.

        Incoming connections from weather stations are forwarded to
        a new thread and handled in a dedicated method.
        """
        self._station_sock.listen()
        while self._serving:
            try:
                conn, addr = self._station_sock.accept()
            except timeout:
                pass
            else:
                print(f"[Accepting connection {addr}]")
                Thread(target=self._station, args=(conn,addr,)).start()

    def _station(self, conn, addr):
        """Where connections to weather stations are handled.
        """
        while self._serving:
            try:
                data = conn.recv(self._BUFFER_SIZE).decode()

                # Make a list of the words in the string in order to extract the city
                list_data = data.split(',')

                # Adding data to database
                self._database.add_data(station=list_data[1], weather_data=data)

                #Saving data to .csv-file
                filename = "%s_weather_station.csv" % list_data[1]
                with open(filename, 'a') as f:
                    writer_object = writer(f)
                    writer_object.writerow(list_data)


                self._data += data+"\n"
            except:
                print(f"[Lost connection to {addr}]")
                break
            if not data:
                break

        print(f"[Closing connection {addr}]")

        conn.close()


if __name__ == "__main__":
    # Get command line flags if we want interactive mode
    parser = argparse.ArgumentParser(description='Run the storage server')
    parser.add_argument("-i", "--interactive", action='store_true')
    args = parser.parse_args(sys.argv[1:])

    load_dotenv()
    server = StorageServer(host=os.getenv("HOST"), port=int(os.getenv("PORT")), main=True, interactive = args.interactive)
    server.turn_on()