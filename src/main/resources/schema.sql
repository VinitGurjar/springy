CREATE TABLE registration (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_number BIGINT NOT NULL,
    user_role VARCHAR(50) DEFAULT 'CUSTOMER'
);

CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price <= 10000),
    quantity INT NOT NULL DEFAULT 0,
    product_description TEXT,
    reserved VARCHAR(10) DEFAULT 'NO',
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES registration(customer_id)
);

CREATE TABLE login_tracking (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    last_login TIMESTAMP,
    last_logout TIMESTAMP,
    updated_password VARCHAR(255),
    old_password VARCHAR(255),
    is_now_logged_in VARCHAR(1) DEFAULT 'N',
    FOREIGN KEY (customer_id) REFERENCES registration(customer_id)
); 