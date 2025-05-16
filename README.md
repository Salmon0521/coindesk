# Coindesk Currency API

這是一個使用 **Spring Boot** 架設的 RESTful API，用於查詢 Coindesk 比特幣匯率資料、轉換格式，以及管理本地貨幣對應表。

## 功能簡述

- 查詢 Coindesk 原始匯率資料
- 將匯率資料轉換為自訂格式（含中文名稱）
- 管理貨幣代碼與中文名稱的對應（CRUD）

---

## API 路徑總覽

| 方法  | 路徑                                     | 說明                       |
|-------|------------------------------------------|----------------------------|
| GET   | `/api/coindesk/raw`                      | 取得 Coindesk 原始匯率資料 |
| GET   | `/api/coindesk/transform`                | 取得轉換後的匯率資料       |
| GET   | `/api/coindesk/currencies`               | 取得所有貨幣對應之中文名   |
| GET   | `/api/coindesk/currencies/{code}`        | 查詢指定貨幣對應之中文名   |
| POST  | `/api/currencies/save`                   | 新增一筆貨幣中文對應資料   |
| PUT   | `/api/currencies/{code}`                 | 更新指定貨幣的中文名稱     |
| DELETE| `/api/currencies/{code}`                 | 刪除指定貨幣對應資料       |

---