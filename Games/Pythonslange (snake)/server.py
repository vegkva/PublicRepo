import socket
from _thread import *
import pickle
import time

server = "localhost"
port = 5555

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

sock.bind((server, port))

try:
    s.bind((server, port))
except socket.error as e:
    str(e)

s.listen(2)
print("Waiting for connection, Server Started")

def read_pos(head_pos):
    try:
        if not head_pos:
            return
        head_pos = head_pos.split(",")
        return int(head_pos[0]), int(head_pos[1]), int(head_pos[2]), int(head_pos[3]), int(head_pos[4])
    except:
        pass

def make_pos(tup):
    return str(tup[0]) + "," + str(tup[1]) + "," + str(tup[2])



pos = [[0, (0,0), (-700,-700)], [0, (100,0), (-700,-700)]]


def threaded_client(conn, player):
    conn.send(str.encode(make_pos(pos[player])))

    while True:
        try:
            data = read_pos(conn.recv(2048).decode('utf-8'))
            pos[player][0] = data[0]
            pos[player][1] = data[1:3]
            pos[player][2] = data[3:5]

            if not data:
                print("Disconnected")
                break
            else:
                if player == 1:
                    reply = pos[0]
                else:
                    reply = pos[1]
            conn.sendall(str.encode(make_pos(reply)))
        except:
            break

    print("Lost connection")
    conn.close()


currentPlayer = 0
while True:
    conn, addr = s.accept()
    print(f"Connected to {addr}")

    start_new_thread(threaded_client, (conn, currentPlayer))

    currentPlayer += 1