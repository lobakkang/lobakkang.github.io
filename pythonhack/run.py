import keyboard
import time
import pyautogui
import os
from pynput.keyboard import Key, Listener

def on_press(key):
         print("bot is stopping")
         os._exit(0)

listener = Listener(on_press=on_press)
listener.start()

print("wait 5 seconds")
time.sleep(5)
print("autoclick started")

while True:
	time.sleep(0.01)
	pyautogui.click(button='right')
