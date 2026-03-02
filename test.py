import simplegui
import random

# Constants
WIDTH = 600
HEIGHT = 400
BALL_RADIUS = 20
PAD_WIDTH = 8
PAD_HEIGHT = 80
HALF_PAD_WIDTH = PAD_WIDTH / 2
HALF_PAD_HEIGHT = PAD_HEIGHT / 2
LEFT = False
RIGHT = True

# Global variables
ball_pos = [WIDTH/2, HEIGHT/2]
ball_vel = [0, 0]
paddle1_pos = HEIGHT/2
paddle2_pos = HEIGHT/2
paddle1_vel = 0
paddle2_vel = 0
score1 = 0
score2 = 0

def spawn_ball(direction):
    global ball_pos, ball_vel
    ball_pos = [WIDTH/2, HEIGHT/2]
    h_vel = random.randrange(120, 240) / 60.0
    v_vel = random.randrange(60, 180) / 60.0
    if direction == RIGHT:
        ball_vel = [h_vel, -v_vel]
    else:
        ball_vel = [-h_vel, -v_vel]

def new_game():
    global score1, score2, paddle1_pos, paddle2