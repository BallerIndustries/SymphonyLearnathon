# User Stories

- Actors: user, requester, bot
- Terms: approval, task

### User
1. As a user I should be able to query the bot for all open (and completed) tasks so that I know what I have to do
1. As a user I should receive a message when I have a task to do so that I know I have to action a task
1. As a user I should be able to approve or reject a task so that the process may continue
1. As a user I should be able to add my comments to my approval or rejection
1. As a user I should be able to query the meta data of a task so that I can get more details on the task, including current approval status
1. As a user I should be able to ask for help from the bot so I know all commands that are available (e.g. help, detail, group, requestors, approvers, external URL)
1. As a user I should be able to chat with other individual participants on the task so I can ask questions
1. As a user I should be able to chat with all participants on the task so I can have a discussion about the task with the group
1. As a user I should be able to join an audio bridge for a task so I can participate in a conversation about the task
1. As a user I should receive a reminder when an audio call is requested so I do not miss the call  (optional: download outlook invite with symphony:// link)
1. As a user I should receive a reminder when a task is almost due or overdue so I don't miss completing a task

### Requester
1. As a requester I should be able to initiate an approval so that participants may be notified that they have tasks to do
1. As a requester I should be able to specify metadata for the approval (title, description, requestors, approvers, due time) so that approvers may receive the information they need to be able to take action (optional: edit metadata)
1. As a requester I should be able to specify a time (in the future or now) for an audio call so participants may be reminded that a call is in progress
1. As a requester I should be able to review the history of a request (including group chat) for audit purposes
1. As a requester I should be able to send reminders to participants with outstanding tasks
1. As a requester I should be able to receive updates when participants update tasks or add comments or all tasks completed (in the tb chat) so that I can know the current status of my approval


### 3 modes
- (b) 1-1 chat with bot
- (tb) 1-1 chat in task room with bot
- (tg) group chat in task room with all participants (not required to open)


### chat bot conversation examples

b > tasks
... (with links to 1-1 and group chat)

b > requests
... (with links to 1-1 and group chat)

tb > help

tb> details

tb> group

tb> approve

tb> reject

tb> comment my comment text goes here


### Architecture

E Approval engine
B Approval bot
UI User Interface: chat, app (stretch)

### Questions

Approval Workflow?
Support incident?
Do we need the desktop app for URLs?
sequential workflow, or single parallel tasks?
app with badges and interactive buttons? Should always have a mobile / chat option.
