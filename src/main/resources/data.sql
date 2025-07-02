-- Admin users
INSERT INTO registration (customer_name, email, password, address, contact_number, user_role) VALUES
('Admin User', 'admin@grocery.com', '$2a$10$encoded_password_here', 'Admin Office, City', 9999999999, 'ADMIN'),
('Root Admin', 'root@grocery.com', '$2a$10$2y5rOZQDFVwOKDO.9DBhEOFWOMdMOG1sSY7GxC49.IMJxBbS26Ife', 'System Admin Office', 8888888888, 'ADMIN');

-- Regular customers
INSERT INTO registration (customer_name, email, password, address, contact_number, user_role) VALUES
('John Customer', 'john@example.com', '$2a$10$encoded_password_here', '123 Main St, USA', 1234567890, 'CUSTOMER');

-- Products
INSERT INTO product (product_name, price, quantity, product_description) VALUES
('Fresh Apples', 2.99, 100, 'Red delicious apples'),
('Organic Bananas', 1.99, 150, 'Fresh organic bananas'),
('Whole Milk', 3.49, 50, 'Fresh whole milk 1 gallon'); 