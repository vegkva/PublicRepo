import socket
import pickle


class Network():
    def __init__(self):
        self.client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server = "localhost"
        self.port = 5555
        self.addr = (self.server, self.port)
        self.pos = self.connect()
        self.head_data = ""
        self.tail_data = ""

    def getPos(self):
        return self.pos

    def connect(self):
        try:
            self.client.connect(self.addr)
            data = self.client.recv(2048).decode()
            return data
        except:
            pass

    def send(self, data):
        try:
            self.client.send(str.encode(data))
            recv = self.client.recv(2048).decode()
            return recv
        except socket.error as e:
            print(e)