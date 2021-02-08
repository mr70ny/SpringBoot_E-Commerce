DROP TABLE IF EXISTS Product;

CREATE TABLE Product (
                              id BIGINT PRIMARY KEY,
                              name VARCHAR(250) NOT NULL,
                              category VARCHAR(250) NOT NULL,
                              retail_price DECIMAL(13, 2) NOT NULL,
                              discounted_price DECIMAL(13, 2) NOT NULL,
                              availability BOOLEAN NOT NULL,
                              created_timeStamp TIMESTAMP WITH TIME ZONE
);