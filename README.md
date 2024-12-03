# Pattern Oriented Software Design - Term project

## notEzKanban

Team members:

- 113598009 李俊威
- 113598029 李致中
- 113598035 莊靜修

## Problem Statement
在快速變動的工作環境中，團隊經常面臨任務可視性不足、優先順序混亂以及缺乏高效協作的挑戰，這些問題導致工作進度延誤和資源浪費。
當前的工作流程缺乏一個系統化的管理方式來提升透明度，幫助團隊明確責任分配，並及時發現和解決瓶頸問題。因此，我們需要一個Kanban系統來將工作流程可視化，
優化任務優先順序，促進高效協作，並提升整體流程的效率和成果。

## Description
本專題將著重在以 Java 開發一個簡單的 Kanban 系統，負責管理團隊的工作流程。這個系統首先需要具備任務卡片管理的能力，
能夠讓使用者創建、編輯、刪除卡片，並透過拖放操作將卡片移動到不同的工作階段（如「待辦事項」、「進行中」和「已完成」）。第二個功能是多用戶協作支援，
透過後端提供的 RESTful API、Socket API，用戶能夠實時查看和更新其他成員的操作結果，確保資料的一致性與同步性。第三個功能是透過通知機制提醒團隊成員關鍵任務的狀態變化，
例如即將逾期或完成的任務。

## Future
此專題未來可以實作Kanban Game功能，讓使用者透過遊戲化的方式學習Kanban的概念，並透過遊戲的方式提升團隊的協作能力。利用即時通訊軟體的Webhook功能，
傳送Kanban即時通知給該看板使用者，讓使用者能在離線時通過手機得知看板資訊。

## Technique

## Design Patterns in Our Code

## ref
[ezKanban](https://gitlab.com/TeddyChen/ezkanban_2020)