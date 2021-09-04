from datetime import datetime, timedelta
from threading import Thread
import os
from station import StationSimulator
from socket import socket
from time import sleep
from dotenv import load_dotenv


class WeatherStation:
    """Class for a simulated weather station client.

    Parameters
    ----------
    host
        Host of server to connect to.
    port
        Port of server to connect to.
    location
        The location of the weather station.
    """

    def __init__(self, host, port:int, location):
        self._host = host
        self._port = port
        self._location = location

        self.date_start = datetime.now()
        self._station = StationSimulator(simulation_interval=2)
        self._station.turn_on()

    def turn_on(self):
        self.connect(self._host, self._port)
        self.send_data()

    def connect(self, host, port):
        """Connect to the server at the given host and port.
        """
        server_address = (host, port)
        self._sock = socket()
        print(server_address)
        try:
            self._sock.connect(server_address)
            print(f"Connection to {server_address}: Established!")
        except:
            print(f"Connection to {server_address}: Failed!")

    def _get_date_time(self, elapsed):
        """Get the current time and date of the station.

        Returns this information correctly formatted in string format.
        """
        time_now = str(elapsed % 24) + ":00"
        date_now = (self.date_start + timedelta(days=elapsed//24)).strftime("%Y-%m-%d")
        return f"{date_now} {time_now}"

    def _get_sensordata(self):
        """Get the current data from the sensors of the station.
        
        Returns a tuple of float values temperature and percipitaion.
        """
        return (self._station.temperature, self._station.rain)

    def _create_data(self, date_time, location, temp, rain):
        """Puts data together ready for transmission to server.
        """
        return f"{date_time},{location},{temp},{rain}"

    def send_data(self):
        while True:
            sleep(2)
            temp, rain = self._get_sensordata()
            data = self._create_data(datetime.now().strftime("%Y-%m-%d %H:%M:%S"), self._location, temp, rain)

            try:
                # check if server is open
                self._sock.sendall("".encode())
            except:
                print("Connection lost!")
                print("Attempting to reconnect in 5 seconds")
                sleep(5)
                self.connect(host=os.getenv("HOST"), port=int(os.getenv("PORT")))
            else:
                self._sock.sendall(data.encode())
                print(f"Sending {data}...")

    def test_capture(self):
        """Test method for sending data.
        """
        for t in range(100):
            sleep(1)
            date_full = self._get_date_time(t)
            temp, rain = self._get_sensordata()
            data = self._create_data(date_full, self._location, temp, rain)

            try:
                # check if server is open
                self._sock.sendall("".encode())
            except:
                print("Connection lost!")
                print("Attempting to reconnect in 5 seconds")
                sleep(5)
                self.connect(host=os.getenv("HOST"), port=int(os.getenv("PORT")))
                continue
            else:
                self._sock.sendall(data.encode())
                print(f"Sending {data}...")
        self._sock.close()

if __name__ == "__main__":
    # Will create four weather stations, instantiate them, and handle them in separate threads
    load_dotenv()
    bergen_station = WeatherStation(host=os.getenv("HOST"), port=int(os.getenv("PORT")), location="Bergen")
    oslo_station = WeatherStation(host=os.getenv("HOST"), port=int(os.getenv("PORT")), location="Oslo")
    krsand_station = WeatherStation(host=os.getenv("HOST"), port=int(os.getenv("PORT")), location="Kristiansand")
    trondheim_station = WeatherStation(host=os.getenv("HOST"), port=int(os.getenv("PORT")), location="Trondheim")

    Thread(target=bergen_station.turn_on).start()
    Thread(target=oslo_station.turn_on).start()
    Thread(target=krsand_station.turn_on).start()
    Thread(target=trondheim_station.turn_on).start()