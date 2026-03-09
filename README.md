## Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Framework  | Spring Boot                         |
| Data       | Spring Data JPA + H2                |
| Web        | Spring Web                          |
| Build      | Maven                               |

---

## Configuration

All business rules are externalised in `src/main/resources/application.properties`:

```properties
# Minimum order amount (€) required for installment eligibility
payment.threshold=100.00

# Fee rate for 4-installment option
payment.four-installment-fee-rate=0.05

# Fixed fee (€) for bank transfer
payment.bank-transfer-fixed-fee=1.00
```

---

## H2 Console

A browser-based SQL console is available at **http://localhost:8080/h2-console**:

| Field    | Value                                          |
|----------|------------------------------------------------|
| JDBC URL | `jdbc:h2:mem:paymentdb;DB_CLOSE_DELAY=-1`      |
| Username | `sa`                                           |
| Password | *(empty)*                                      |

---

## API Reference

### Orders

| Method | Endpoint          | Description         |
|--------|-------------------|---------------------|
| POST   | `/api/orders`     | Create a new order  |
| GET    | `/api/orders`     | List all orders     |
| GET    | `/api/orders/{id}`| Get order by id     |

**Create Order — request body**
```json
{
  "customerName": "Mostafa",
  "amount": 250.00
}
```

**Create Order — response**
```json
{
  "id": 1,
  "customerName": "Mostafa",
  "amount": 250.00,
  "createdAt": "2024-03-01T10:00:00",
  "eligibleForInstallments": true,
  "paymentPlan": null
}
```

---

### Payment Plans

| Method | Endpoint                           | Description                              |
|--------|------------------------------------|------------------------------------------|
| POST   | `/api/orders/{id}/payment-plan`    | Create a payment plan for an order       |
| GET    | `/api/orders/{id}/payment-plan`    | Retrieve the plan + schedule for an order|

**Create Payment Plan — request body**
```json
{
  "paymentOption": "THREE_INSTALLMENTS"
}
```

Available values for `paymentOption`:
- `THREE_INSTALLMENTS` — 3 equal payments, no fees
- `FOUR_INSTALLMENTS`  — 4 equal payments + 5% fee
- `BANK_TRANSFER`      — single payment + 1 € fixed fee

**Create Payment Plan — response**
```json
{
  "id": 1,
  "orderId": 1,
  "paymentOption": "THREE_INSTALLMENTS",
  "paymentOptionLabel": "3 installments – no fees",
  "originalAmount": 250.00,
  "feeAmount": 0.00,
  "totalAmount": 250.00,
  "createdAt": "2024-03-01T10:05:00",
  "installments": [
    { "installmentNumber": 1, "amount": 83.33, "dueDate": "2024-03-01", "paid": false },
    { "installmentNumber": 2, "amount": 83.33, "dueDate": "2024-04-01", "paid": false },
    { "installmentNumber": 3, "amount": 83.34, "dueDate": "2024-05-01", "paid": false }
  ]
}
```

---

## Error Responses

All errors follow a consistent shape:

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Order amount 50.00 € does not exceed the installment threshold of 100.00 €",
  "timestamp": "2024-03-01T10:00:00",
  "details": null
}
```

| HTTP Status | Scenario                                        |
|-------------|-------------------------------------------------|
| 400         | Validation failure (missing / invalid fields)   |
| 404         | Order not found                                 |
| 409         | Payment plan already exists for that order      |
| 422         | Order amount below the installment threshold    |
| 500         | Unexpected server error                         |
