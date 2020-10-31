# Additional Material

Events & Handler / Observer / Signal and Slot Pattern 
```py
class Event:
    handlers: [Handler]

    def raise_event(self):
        for handler in handlers:
            handler.handle(self)
        # Synchronus

    def add_handler(self, handler):
        self.handlers.append(handler)

class Handler:
    def handle(self, event: Event):
        pass

# Observer Pattern
# Event & Handlers
# Slots & Signals
# Events & Observer

class Button:
    click_event: Event

    def on_click(self):
        self.click_event.raise_event()

class Window:
    def __init__(self):
        self.button = Button()
        self.button.click_event.add_handler(self.button_click_handler)

    def button_click_handler(event: Event):
        print("button was clicked")


```


-- --


Pub-Sub pattern

```py
# Process 1
class Publisher:
    pass
    # async


# Process 2
class Consumer:
    def process_message(self, message):
        pass


# Process 3 - Kafka / Celery
class Queue:
    messages: List[str]

    def send_message(self, message):
        # add to queue
        messages.append(message)

    def get_message(self):
        # if not empty, then get any from queue
        if messages:
            return messages.pop()
```