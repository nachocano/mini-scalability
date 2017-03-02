from locust import HttpLocust, TaskSet, task
import random

class UserBehavior(TaskSet):
    def on_start(self):
        """ on_start is called when a Locust start before any task is scheduled """
        pass

    @task
    def my_task(self):
        from gevent.pool import Group
        group = Group()
        group.spawn(user)
        group.spawn(query)
        group.join()

    def user(self):
        uid = random.randint(0, 10000000000)
        self.client.put("/user/{}".format(uid))
        self.client.delete("/user/{}".format(uid))

    def query(self):
        qid = random.randint(0, 10000000000)
        self.client.put("/query/{}".format(qid))
        self.client.delete("/query/{}".format(qid))

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 0
    max_wait = 4000
