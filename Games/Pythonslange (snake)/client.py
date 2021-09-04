import time
import turtle
from turtle import Turtle
from random import randrange
from _thread import *
from all_in_one_network import Network
from socket import socket, AF_INET, SOCK_DGRAM
import re
import pickle

sock = socket(AF_INET, SOCK_DGRAM)

up_ = False
down_ = False
left_ = False
right_ = False

eatBool = 0
tailCount = 0

collision = 0

gameOver = False
reset = False

n = 40 ## border control

score = 0

highScore = score

'''Window'''
wn = turtle.Screen()
wn.title("Snake")
wn.bgcolor("black")
wn.setup(width=1000, height=700)
wn.tracer(0)

class turtle_object(Turtle):
    def __init__(self, position, shape, color):
        global displacement
        super().__init__(shape=shape, visible=False, )
        self.color(color)
        self.penup()
        self.setposition(position)
        self.speed(20)
        self.showturtle()
        self.dy = displacement
        self.dx = displacement
        self._x = 0 # previous x
        self._y = 0 # previous y


displacement = 10# pixles


def read_pos(head_string):
    head_pos = re.split(r',\s*(?![^()]*\))', head_string)
    headPos = head_pos[1].replace('(', '').replace(')', '')
    tailPos = head_pos[2].replace('(', '').replace(')', '')
    resHead = tuple(map(float, headPos.split(', ')))
    resTail = tuple(map(float, tailPos.split(', ')))
    eatBool = head_pos[0]
    return tuple(eatBool) + resHead + resTail

def make_pos(tup):
    output = str(tup[0]) + "," + str(tup[1]) + "," + str(tup[2]) + "," + str(tup[3])  + "," + str(tup[4])
    return output


network = Network()
startPos = read_pos(network.getPos())


'''Instantiate snake and food'''
head1 = turtle_object((startPos[1], startPos[2]), "square", "red")
head1.setpos(20, 20)
head2 = turtle_object((50, 50), "square", "white")
food = turtle_object((-500, 500), "turtle", "yellow")
tail = [turtle_object((startPos[3],startPos[4]), "square", "blue")]
tail2 = []
tail3 = []


'''Position of the food'''
positions = [(randrange(-300, 300, 10), randrange(-300, 300, 10))]


def play():
    if right_:
        move("d")
        instructions.clear()
    if left_:
        move("a")
        instructions.clear()
    if up_:
        move("w")
        instructions.clear()
    if down_:
        move("s")
        instructions.clear()

def move(key):
    global down_, right_, up_, left_
    x, y = head1.pos()
    head1._x, head1._y = head1.pos()
    global direction
    if key == "w":          # Moving up
        if not down_:
            up_ = True
            right_ = left_ = down_ = False
            head1.sety(y + displacement)
    if key == "s":          # Moving down
        if not up_:
            down_ = True
            right_ = up_ = left_ = False
            head1.sety(y - displacement)
    if key == "d":          # Moving right
        if not left_:
            right_ = True
            left_ = up_ = down_ = False
            head1.setx(x + displacement)
    if key == "a":          # Moving left
        if not right_:
            left_ = True
            right_ = up_ = down_ = False
            head1.setx(x - displacement)
    if len(tail) > 1:
        moveTail()
    moveTail2()

def addTail():
    if tail.count(turtle_object) == 0:
        x = head1._x
        y = head1._y
        tail.append(turtle_object((x, y), "square", "blue"))

def moveTail():
    x = head1._x
    y = head1._y
    for element in tail:
        element._x, element._y = element.pos()
        element.setpos(x, y)
        x = element._x
        y = element._y

def moveTail2():
    h2x, h2y = head2.pos()
    head2._x, head2._y = head2.pos()
    for element in tail2:
        element._x, element._y = element.pos()
        element.setpos(h2x, h2y)
        h2x = element._x
        h2y = element._y

def upper_border():
    global n
    if head1.ycor() > 370 + n:
        head1.sety(-350)

def lower_border():
    global n
    if head1.ycor() < -350 - n:
        head1.sety(350)

def left_border():
    global n
    if head1.xcor() < -550 - n:
        head1.setx(530)

def right_border():
    global n
    if head1.xcor() > 550 + n:
        head1.setx(-530)

def send_receive():
    print(tail)
    head2Pos = read_pos(network.send(make_pos(
        (eatBool, int(head1.xcor()), int(head1.ycor()), int(tail[tailCount].xcor()), int(tail[tailCount].ycor())))))
    head2.setx(head2Pos[1])
    head2.sety(head2Pos[2])

def update_tail():
    global eatBool, head2, reset

    head2Pos = read_pos(network.send(make_pos(
        (eatBool, int(head1.xcor()), int(head1.ycor()), int(tail[tailCount].xcor()), int(tail[tailCount].ycor())))))
    if reset:
        print(tail2)
        head2 = turtle_object((head2Pos[1], head2Pos[2]), "square", "white")
        reset = False
    head2.setx(head2Pos[1])
    head2.sety(head2Pos[2])
    eatBool = int(head2Pos[0])
    if not tail2:
        tail2.append(turtle_object((head2Pos[3], head2Pos[4]), "square", "white"))
    if tail2:
        if tail2[-1].xcor() == head2Pos[2] and tail2[-1].ycor() == head2Pos[3]:
            pass
        else:
            if eatBool == 1:

                print("eat")
                tail2.append(turtle_object((head2Pos[2], head2Pos[3]), "square", "green"))
                eatBool = 0


def check_collision():
    global reset, collision
    if not reset:
        for element in tail2:
            if head1.xcor() == startPos[1] and head1.ycor() == startPos[2]:
                break
            if head1.xcor() - 20 < element.xcor() < 20 + head1.xcor() and head1.ycor() - 20 < element.ycor() < 20 + head1.ycor():
                print(element.xcor(), element.ycor())
                game_over()
                reset = False
                break
    # if head1.xcor() - 20 < head2.xcor() < 20 + head1.xcor() and head1.ycor() - 20 < head2.ycor() < 20 + head1.ycor():
    #     game_over()
    # if head2.xcor() - 20 < head1.xcor() < 20 + head2.xcor() and head2.ycor() - 20 < head1.ycor() < 20 + head2.ycor():
    #     game_over()

def reset_screen():
    global tail, score, head1, head2, food, tail2, gameOver, tailCount, reset
    reset = True
    tailCount = 0
    tail.clear()
    wn.reset()
    score = 0
    head1 = turtle_object((0, 0), "square", "orange")
    food = turtle_object((-500, 500), "turtle", "yellow")
    tail = [turtle_object((-700, -700), "square", "blue")]
    #update_tail()
    send_receive()
    pen = turtle.Turtle()  #
    pen.speed(0)  #
    pen.color("white")  # <---- TODO: Simplify this code
    pen.penup()  #
    pen.hideturtle()  #
    pen.goto(0, 300)  #
    pen.write(f"Score: 0\t Highscore: {highScore}", align="center", font=("Courier", 26, "normal"))

    gameOver = False
    print("¤¤")

wn.listen()
commands = ["w", "a", "s", "d"]

for k in commands:
    wn.onkeypress(lambda k=k: move(k), k)

'''Score system'''
pen = turtle.Turtle()
pen.speed(0)
pen.color("white")
pen.penup()
pen.hideturtle()
pen.goto(0, 300)
pen.write(f"Score: 0\t Highscore: {highScore}", align="center", font=("Courier", 26, "normal"))

def game_over():

    global highScore, gameOver, up_, down_, left_, right_
    gameOver = True
    up_ = False
    down_ = False
    left_ = False
    right_ = False
    pen.goto(0, 0)
    pen.write("GAME OVER!", align="center", font=("Courier", 50, "normal"))
    time.sleep(2)
    if score > highScore:
        highScore = score
    else:
        highScore = highScore
    print("game over")

'''Instructions'''
instructions = turtle.Turtle()
instructions.speed(0)
instructions.color("white")
instructions.penup()
instructions.hideturtle()
instructions.goto(-50, -150)
instructions.write("DIRECTIONS\nup - 'w'\nleft - 'a'\ndown - 's'"
                   "\nright - 'd'\n\n"
                   "\n\n\n\n\n\n\n\n\n\n\n\tstart moving the snake by pressing a direction-key", align="center",
                   font=("Courier", 16, "normal"))

while True:



    if gameOver:
        print(gameOver)
        reset_screen()
        collision = 1

    update_tail()

    if collision == 0:
        check_collision()

    play()

    '''This is making the while loop slower so that the 
    tail is placed correctly, slower snake = add more sleep-statements'''
    speed = [time.sleep(0.001), time.sleep(0.001)]

    right_border()
    left_border()
    lower_border()
    upper_border()
    fontSize = 50
    for x in tail:
        try:
            if head1.xcor() - 10 < x.xcor() < 10 + head1.xcor() and head1.ycor() - 10 < x.ycor() < 10 + head1.ycor():
                gameOver = True
                up_ = False
                down_ = False
                left_ = False
                right_ = False
                pen.goto(0, 0)
                pen.write("GAME OVER!", align="center", font=("Courier", 50, "normal"))
                time.sleep(2)
                if score > highScore:
                    highScore = score
                else:
                    highScore = highScore
                break


        except:
            pass

    '''FOOD'''
    for position in positions:
        food.goto(position)  # placing food at random location on the screen

        '''Checks if the snake hits the x,y-coordinates of the food'''
        if head1.xcor() - 20 < food.xcor() < 20 + head1.xcor() and head1.ycor() - 20 < food.ycor() < 20 + head1.ycor():
            pen.clear()
            try:
                collision = 0
                eatBool = 1
                addTail()
                positions.remove(position)
                tailCount += 1
            except:
                print("no elements in list")
                continue
            positions.append((randrange(-300, 300, 10), randrange(-300, 300, 10)))  # adding new food

            score += 1
            pen.goto(0,300)
            pen.write(f"Score: {score}\t Highscore: {highScore}", align="center", font=("Courier", 26, "normal"))


    wn.update()
