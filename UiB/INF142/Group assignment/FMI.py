import os
import json
import traceback
from socket import socket, AF_INET, SOCK_DGRAM
from time import sleep
from tkinter import messagebox

from tkinter import *
from dotenv import load_dotenv

class FMI:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket(AF_INET, SOCK_DGRAM)
        self.station = ""

    def request(self, station):
        """Requests stations data from database server/storage server
        """
        try:
            if (station == None):
                data = "GET".encode()
            else:
                data = ("GET "+station).encode()
            self.sock.sendto(data, (os.getenv("HOST"), int(os.getenv("PORT"))))
            data, address = self.sock.recvfrom(2000*80)
            data = json.loads(data.decode())
            return data
        except:
            traceback.print_exc()
            return "Error requesting data, or no data available"
            pass

    # User has the possibility to save the data stored in server into a file.
    def save_to_file(self):
        try:
            self.sock.connect((self.host, self.port))
            self.sock.sendto("GET".encode(), (os.getenv("HOST"), int(os.getenv("PORT"))))
            data, address = self.sock.recvfrom(2000*8)
            data = data.decode()
            data = data.replace('",', '\n').replace('"]', "").replace('["', "").replace('"', "").replace(' ', "")

            with open("weather1_data.csv", 'w') as f:
                f.write(data)
            print(f"saving {data} to file")
            sleep(1)
        except:
            pass

    """When a button is pressed, this function creates a window with the data from :param station """
    def gui_table(self, station):

        lst = self.request(station)

        #create the root window (widget)
        root = Tk()

        if station == "Server":
            root.geometry("500x500")
            root.title("All data stored in server")
        else:
            root.title(f"Weather data for {station} (database)")
        root.config(bg="grey")

        #create scrollbar
        sb = Scrollbar(root)
        sb.pack(side=RIGHT, fill=Y)


        #insert values into window
        my_database = Text(root, yscrollcommand=sb.set, bg="black", fg="white")
        my_database.insert(END, "\t" + "Date" + "\t" + "City" + "\t" + "Temp,Precip" + "\n")
        for x in lst:
            my_database.insert(END, x + '\n')

        sb.config(command=my_database.yview)
        #inserting the data into the window
        my_database.pack(side=LEFT, fill=BOTH, expand=1)
        root.mainloop()

    # Creating the main menu window
    def main_menu_gui(self):
        root = Tk()
        label = Label(text="Welcome! \n Choose from which city to view weather data", width=50, height=5)
        label.config(font=("Arial", 25), bg="grey")
        label.pack()
        root.config(bg="grey", height=20)

        quit = Button(root, text="Quit", padx=20, pady=10, font=("Arial", 10), bg="black", fg="red", command=exit).pack(side=BOTTOM)
        bergen_weather = Button(root,  text="Bergen", font=("Arial", 20), padx=20, pady=20, bg="black", fg="white", command=lambda: FMIClient.gui_table(station="Bergen")).pack(side=LEFT)
        oslo_weather = Button(root,  text="Oslo", font=("Arial", 20), padx=20, pady=20, bg="black", fg="white", command=lambda: FMIClient.gui_table(station="Oslo")).pack(side=LEFT)
        trondheim_weather = Button(root,  text="Trondheim", font=("Arial", 20), padx=20, pady=20, bg="black", fg="white",
                                   command=lambda: FMIClient.gui_table(station="Trondheim")).pack(side=LEFT)
        kristiansand_weather = Button(root,  text="Kristiansand", font=("Arial", 20), padx=20, pady=20, bg="black", fg="white",
                                      command=lambda: FMIClient.gui_table(station="Kristiansand")).pack(side=LEFT)

        curr_data = Button(root, text="View current data in the server",font=("Arial", 10), padx=20, pady=35, bg="black", fg="white", command=lambda: FMIClient.gui_table(None))

        save_data = Button(text="Save current data to file",font=("Arial", 10), padx=20, pady=35, bg="black", fg="white", command=lambda: FMIClient.save_to_file())


        save_data.pack(side=RIGHT)
        curr_data.pack(side=RIGHT)

        root.mainloop()


    def user_choice(self):
        while True:
            print("r: Request data\nq: Quit\ngui: Graphical User Interface")
            cmd = input("r/q/gui: ")
            if cmd == "q":
                exit()
            elif cmd == "r":
                print(type(FMIClient.request()))
            elif cmd == "gui":
                FMIClient.main_menu_gui()

if __name__ == "__main__":
    # Instantiate database
    load_dotenv()

    FMIClient = FMI(host=os.getenv("HOST"), port=int(os.getenv("PORT")))
    FMIClient.main_menu_gui()
    #FMIClient.user_choice()

