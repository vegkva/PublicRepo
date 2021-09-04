import hashlib
from PIL import Image
import time

from tkinter import Tk
from tkinter import filedialog

"""The original assignment was to implement a simplified procedure of secure http 
response from Alice (web server) to Bob (web client) in an insecure environment

I have now made it so it can encrypt and decrypt text and images.

This program will decrypt text or image to its original contents.
secretImage.txt is an example image to use with this code
"""

start_time = time.time()

def decrypt_picture():
    with open("decrypted.jpg", "r+b") as f:
        first_line = f.readline(40)
        if b'Exif' in first_line:
            print("Picture found!")
            elapsed_time = time.time() - start_time
            print(f"Elapsed time: {round(elapsed_time)} seconds.")
            open_picture = input(print("Do you want to open the secret picture? y/n\n"))
            if open_picture == "y":
                img = Image.open('decrypted.jpg')
                img.show()
            exit()
        if b'JFIF' in first_line:
            print("Picture found!")
            elapsed_time = time.time() - start_time
            print(f"Elapsed time: {round(elapsed_time)} seconds.")
            open_picture = input(print("Do you want to open the secret picture? y/n\n"))
            if open_picture == "y":
                img = Image.open('decrypted.jpg')
                img.show()
            exit()
    f.close()

# def decrypt_text():
#     with open("decrypted.txt", "r+b") as b:
#         mmn = mmap.mmap(b.fileno(), 0)
#         i = mmn[0:1000]
#         try:
#             i.decode()
#             print("Text file found!")
#             print(b.read().decode())
#             exit()
#         except (UnicodeDecodeError, AttributeError):
#             pass

def rc4():

    key = alice.to_bytes(2, 'big')

    key_to_bytes = hashlib.sha256(key).digest()

    S = list(range(256))
    keystream = bytearray()
    ciphertext = bytearray()

    j = 0
    for i in range(256):
        j = (j + S[i] + key_to_bytes[i % len(key_to_bytes)]) % 256
        S[i], S[j] = S[j], S[i]

    i = 0
    j = 0
    x = 0
    while (x < len(cipher_text)):
        i = (i + 1) % 256
        j = (j + S[i]) % 256
        S[i], S[j] = S[j], S[i]
        K = S[(S[i] + S[j]) % 256]
        keystream.append(K)
        ciphertext.append(cipher_text[x] ^ keystream[x])
        x += 1


    if choice == "1":
        try:
            fp = open("decrypted.txt", "wb")
            fp.write(ciphertext)
            fp.close()
        except:
            pass
    if choice == "2":
        try:
            fp = open("decrypted.jpg", "wb")
            fp.write(ciphertext)
            fp.close()
        except:
            pass



choice = input(print("Decrypt text (1) or image (2)\n"))

Tk().withdraw()
f = open(
    filedialog.askopenfilename(initialdir="", title="Select file to decrypt", filetypes=(("text files", "*.txt"),)),
    "rb")
cipher_text = f.read() #
f.close()

# your code
counter = 0
for x in range(0, 255):
    alice = x
    p = 187
    g = 2
    counter += 1
    if counter == 10:
        print("...", end="", flush=True)
        counter = 0
    rc4()


    if choice == "2":
        decrypt_picture()


print("a")





