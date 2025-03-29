# Coupon API README

## **Setup Instructions**
1. Clone the repository.
2. Configure PostgreSQL connection in `application.properties`.
3. Run `mvn spring-boot:run`.

## Implemented Cases

1.  **Cart-wise Coupons:**
    * Implemented discount application based on a cart total threshold.
    * Example: 10% off on carts over Rs. 100.
2.  **Product-wise Coupons:**
    * Implemented discount application for specific products.
    * Example: 20% off on Product A.
3.  **Basic API Endpoints:**
    * Implemented CRUD operations for coupons (POST, GET, PUT, DELETE).
    * Implemented endpoint for fetching applicable coupons.
    * Implemented endpoint for applying a specific coupon.

## Unimplemented Cases

1.  **BxGy Coupons:**
    * The logic for BxGy coupons is complex and requires handling buy and get product lists, quantities, and repetition limits.
    * The `calculateBxGyDiscount` and `applyBxGyDiscount` methods are placeholders and need further implementation.
    * Difficult to implement fully within time constraints.
2.  **Advanced Coupon Constraints:**
    * Coupons with start and end dates.
    * Coupons that can only be applied to specific user groups.
    * Coupons that only apply to the most expensive item in a cart.
    * Coupons with a maximum discount amount.
    * Coupons with combined conditions (e.g. cart total > 100 and product A in cart).
3.  **Coupon Combinations:**
    * Handling multiple coupons applied to the same cart and resolving conflicts.

## Limitations

1.  **BxGy Implementation:**
    * The BxGy coupon logic is not fully implemented.
2.  **Advanced Constraints:**
    * Advanced coupon constraints are not implemented.
3.  **Error Handling:**
    * Basic error handling is implemented, but more robust error handling is needed for production use.
4.  **Database interactions:**
    * The database interactions are simple, and do not contain complex queries.
5.  **Performance:**
    * The performance of the API has not been extensively tested or optimized.

## Assumptions

1.  **Cart Structure:**
    * The cart structure is assumed to be a JSON object with an "items" array, where each item has "product_id," "quantity," and "price" fields.
2.  **Coupon Details:**
    * Coupon details are stored as JSON objects in the database.
3.  **Simple Discount Logic:**
    * Discount values are assumed to be percentages.
4.  **Database Connectivity:**
    * PostgreSQL database is running on localhost:5432 with the provided credentials.
5.  **No Authentication:**
    * The API does not implement any form of authentication or authorization.

## **Endpoints**
| Method | Endpoint | Description |
|--------|---------|------------|
| `POST` | `/coupons` | Create a new coupon |
| `GET` | `/coupons` | Retrieve all coupons |
| `GET` | `/coupons/{id}` | Retrieve a specific coupon |
| `PUT` | `/coupons/{id}` | Update a coupon |
| `DELETE` | `/coupons/{id}` | Delete a coupon |
| `POST` | `/applicable-coupons` | Get all applicable coupons for a cart |
| `POST` | `/apply-coupon/{id}` | Apply a coupon to a cart |
