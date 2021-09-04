from pwn import *


"""First exercise in using pwntools - INF226 UiB 2021"""



# Connects to the server
r = remote('158.39.77.181', 6001)


# The server asks for a secret sign at first (which is 'infA')
data = r.recvline()
if data:
	r.send(b'infA')


recv = r.recv(100)

	
def two_numbers(calculation):
    byte_to_string = calculation.decode()
    result = 0
    numbers = byte_to_string.split(" ")
    if numbers[1] == "+":
        result = int(numbers[0]) + int(numbers[2])
    if numbers[1] == "*":
        result = int(numbers[0]) * int(numbers[2])
    return str(result).encode() + b'\n'

# The server sends 1024 random arithmetic problem (addition and multiplication) and
# the function two_numbers take 'recv' as input and returns the answer"""

while recv:
	print(recv)
	recv = r.recv(17)
	send = r.send(two_numbers(recv))
	
