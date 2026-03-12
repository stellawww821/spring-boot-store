INSERT INTO categories (name) VALUES
    ('Electronics'),
    ('Books'),
    ('Clothing'),
    ('Home & Kitchen'),
    ('Sports');

INSERT INTO products (name, price, description, category_id) VALUES
    ('Wireless Mouse', 25.99, 'Ergonomic wireless mouse with USB receiver', 1),
    ('Bluetooth Headphones', 79.99, 'Noise cancelling over-ear headphones', 1),

    ('Java Programming Guide', 39.99, 'Comprehensive guide to modern Java development', 2),
    ('Spring Boot in Action', 45.50, 'Learn how to build APIs with Spring Boot', 2),

    ('Men''s Casual T-Shirt', 19.99, 'Comfortable cotton t-shirt for daily wear', 3),
    ('Women''s Denim Jacket', 59.99, 'Stylish blue denim jacket', 3),

    ('Stainless Steel Pan', 34.99, 'Durable stainless steel frying pan', 4),
    ('Coffee Maker', 89.99, 'Automatic drip coffee maker', 4),

    ('Yoga Mat', 22.50, 'Non-slip yoga mat for exercise', 5),
    ('Dumbbell Set', 120.00, 'Adjustable dumbbell set for strength training', 5);