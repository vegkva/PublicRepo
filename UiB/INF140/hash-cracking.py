import hashlib
import datetime
import time
from sys import exit
from itertools import product, chain


"""
@Author: vegkva
14/03/2020 

***********************************************************************************
Assignment: 
Function 1: ask user to input a password user-passwordwith the format “uuudddd",
where ‘u’ denotes upper-case letter and ‘d’ denotes a digit from 0-9; and output the
hash value of SHA256(user-password) in hexadecimal form (1 pt)
Function 2: a brute-force function that outputs the X with the format “uuudddd" such
that SHA256(X) = SHA256(user-password) in Function 1 (2 pts)
***********************************************************************************

A program that takes a user input in the format of UUDDDD where U = uppercase leather and D = digit 0-9.
The program computes the hash value and brute_force() then brute force the hash
"""


def print_hash_value():
    print("Enter a password containing three upper case letters and four digits. In this order: 'AAA0000': ")
    password = input().upper()

    if len(password) != 7:
        print("Password must contain 7 characters, try again.")
        brute_force()
    elif password.isdigit() or password.isalpha():
        print("Password must contain letters AND digits")
        brute_force()

    print("You entered password: " + password)
    m = hashlib.sha256(password.encode('utf-8')).hexdigest()
    hex_password = m
    print("Hash value of password " + "'" + password + "'" + " in hexidecimal form: " + hex_password)
    return password



def brute_force():
    password = print_hash_value()
    password_hash = hashlib.sha256(password.encode('utf-8')).hexdigest()
    tic = time.perf_counter()
    print("Cracking, please wait", end="", flush=True)
    counter = 0

    for a in range(ord('A'), ord('Z') + 1):

        for b in range(ord('A'), ord('Z') + 1):

            for c in range(ord('A'), ord('Z') + 1):
                counter += 1
                for n in range(0, 9999 + 1):
                    x = chr(a) + chr(b) + chr(c) + str(n).zfill(4)

                    m = hashlib.sha256(x.encode('utf-8')).hexdigest()

                    if (counter > 70):
                        print(".", end="", flush=True)
                        counter = 0

                    # Printing status when loop reaches KAA0000
                    if x == "KAA0000":
                        toc2 = time.perf_counter()
                        print("")
                        time_status = toc2 - tic
                        if time_status < 60:
                            print(f"Time elapsed: {toc2 - tic:0.2f} seconds. Checking K******, please keep waiting...")
                        if (60 <= time_status < 120):
                            one_minute_status(time_status)
                        if (time_status >= 120):
                            two_minutes_status(time_status)
                        print("Cracking continues, please wait", end='')

                    if password_hash == m:
                        print("")
                        print('Cracking complete! The password is:', x)
                        toc = time.perf_counter()
                        time1 = toc-tic
                        if time1 < 60:
                            print(f"Cracked the password in {time1:0.2f} seconds")
                        if (60 <= time1 < 120):
                            one_minute(time1)
                        if (time1 >= 120):
                            two_minutes(time1)

                        text = input("Enter 'x' to exit. Push 'ENTER' to input another password\n")
                        if text != "x":
                            brute_force()
                        else:
                            exit(0)


def one_minute(time1):
    minutes = str(round(time1 / 60))
    seconds = str(round(time1 % 60))

    if seconds == 1:
        print("Cracked the password in " + minutes + " minute and " + seconds + " second")
    else:
        print("Cracked the password in " + minutes + " minute and " + seconds + " seconds")

def two_minutes(time1):
    minutes = str(round(time1 / 60))
    seconds = str(round(time1 % 60))
    if seconds == 1:
        print("Cracked the password in " + minutes + " minutes and " + seconds + " second")
    else:
        print("Cracked the password in " + minutes + " minutes and " + seconds + " seconds")


def one_minute_status(time_status):
    minutes = str(round(time_status / 60))
    seconds = str(round(time_status % 60))

    if seconds == 1:
        print("Time elapsed: " + minutes + " minute and " + seconds + " second. Checking M******, please keep on waiting...")
    else:
        print("Time elapsed: " + minutes + " minute and " + seconds + " seconds. Checking M******, please keep on waiting...")


def two_minutes_status(time_status):
    minutes = str(round(time_status / 60))
    seconds = str(round(time_status % 60))
    if seconds == 1:
        print("Time elapsed: " + minutes + " minutes and " + seconds + " second. Checking M******, please keep on waiting...")
    else:
        print("Time elapsed: " + minutes + " minutes and " + seconds + " seconds. Checking M******, please keep on waiting...")


print(brute_force())
