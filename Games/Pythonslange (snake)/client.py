import json
import time
import turtle
from turtle import Turtle
from random import randrange
from networkClass import Network
from socket import socket, AF_INET, SOCK_DGRAM

sock = socket(AF_INET, SOCK_DGRAM)

up_ = False
down_ = False
left_ = False
right_ = False

gameOverBool = False
eatBool = False
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


def send_json():
    dictdata = {
        "eatBool": eatBool,
        "gameOverBool": gameOverBool,
        "headX": head1.xcor(),
        "headY": head1.ycor(),
        "tailX": tail[tailCount].xcor(),
        "tailY": tail[tailCount].ycor(),
        "foodX": food.xcor(),
        "foodY": food.ycor(),
        "score": score
    }
    dictJson = json.dumps(dictdata)

    return network.send(dictJson)

network = Network()
startPos = json.loads(network.getPos())


'''Instantiate snake and food'''
head1 = turtle_object((startPos["headX"], startPos["headY"]), "square", "red")

head2 = turtle_object((50, 50), "square", "white")
food = turtle_object((startPos["foodX"], startPos["foodY"]), "turtle", "yellow")
tail = [turtle_object((startPos["tailX"],startPos["tailY"]), "square", "blue")]
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

def borders():
    global n
    #Upper
    if head1.ycor() > 330 + n:
        head1.sety(-340)
    #Lower
    if head1.ycor() < -330 - n:
        head1.sety(340)
    #Left
    if head1.xcor() < -480 - n:
        head1.setx(490)
    #Right
    if head1.xcor() > 480 + n:
        head1.setx(-490)



def update_tail():
    global eatBool, head2, reset, gameOverBool, tail2

    player2Data = json.loads(send_json())
    if reset:
        for element in tail2:
            element.showturtle()
        head2 = turtle_object((player2Data["headX"], player2Data["headY"]), "square", "white")
        reset = False
    head2.setx(player2Data["headX"])
    head2.sety(player2Data["headY"])
    eatBool = player2Data["eatBool"]
    gameOverBool = player2Data["gameOverBool"]
    if not tail2:
        tail2.append(turtle_object((player2Data["tailX"], player2Data["tailY"]), "square", "white"))
    if tail2:
        if tail2[-1].xcor() == player2Data["headX"] and tail2[-1].ycor() == player2Data["headY"]:
            pass
        else:
            if eatBool:

                tail2.append(turtle_object((player2Data["tailX"], player2Data["tailY"]), "square", "green"))
                eatBool = False


def check_collision():
    global reset, collision
    if not reset:
        for element in tail2:
            if head1.xcor() == startPos["headX"] and head1.ycor() == startPos["headY"]:
                break
            if head1.xcor() - 20 < element.xcor() < 20 + head1.xcor() and head1.ycor() - 20 < element.ycor() < 20 + head1.ycor():
                print(element.xcor(), element.ycor())
                game_over()
                break
    # if head1.xcor() - 20 < head2.xcor() < 20 + head1.xcor() and head1.ycor() - 20 < head2.ycor() < 20 + head1.ycor():
    #     game_over()
    # if head2.xcor() - 20 < head1.xcor() < 20 + head2.xcor() and head2.ycor() - 20 < head1.ycor() < 20 + head2.ycor():
    #     game_over()

def reset_screen():
    global tail, score, head1, head2, food, tail2, gameOver, tailCount, reset, up_
    reset = True
    tailCount = 0
    tail.clear()
    wn.reset()
    score = 0
    head1 = turtle_object((0, 0), "square", "orange")
    tail = [turtle_object((-700, -700), "square", "blue")]
    tail2 = tail3.copy()
    pen = turtle.Turtle()  #
    pen.speed(0)  #
    pen.color("white")  # <---- TODO: Simplify this code
    pen.penup()  #
    pen.hideturtle()  #
    pen.goto(0, 300)  #
    pen.write(f"Score: 0\t Highscore: {highScore}", align="center", font=("Courier", 26, "normal"))
    gameOver = False
    up_ = True
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

    global highScore, gameOver, up_, down_, left_, right_, gameOverBool, collision, tail3
    gameOver = True
    gameOverBool = True
    tail3 = tail2.copy()
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
    reset_screen()
    collision = 1

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

    # if collision == 0:        Collision between players not yet working
    #     check_collision()

    update_tail()

    if gameOverBool:
        print("player2 game over")
        tail2.clear()
        head2.clear()
        gameOverBool = False
        reset = False
        collision = 0


    play()

    '''This is making the while loop slower so that the 
    tail is placed correctly, slower snake = add more sleep-statements'''
    speed = [time.sleep(0.001), time.sleep(0.001)]

    borders()
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
                eatBool = True
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
