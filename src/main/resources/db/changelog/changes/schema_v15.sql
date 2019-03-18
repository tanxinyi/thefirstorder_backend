CREATE TABLE IF NOT EXISTS Admin (
  admin_id VARCHAR(10) NOT NULL,
  admin_password VARCHAR(50) NOT NULL,
  convert_to_points DOUBLE PRECISION NOT NULL,
  convert_from_points DOUBLE PRECISION NOT NULL,
  stripe_token VARCHAR(50) NOT NULL,
  PRIMARY KEY (admin_id));

CREATE TABLE IF NOT EXISTS Restaurant(
  restaurant_id VARCHAR(10) NOT NULL,
  admin_id VARCHAR(10) NOT NULL,
  restaurant_name VARCHAR(45) NOT NULL,
  restaurant_description VARCHAR(500) NOT NULL,
  restaurant_contact_number VARCHAR(45) NOT NULL,
  restaurant_opening_hours VARCHAR(500) NOT NULL,
  building VARCHAR(500) NOT NULL,
  street VARCHAR(500) NOT NULL,
  postal_code VARCHAR(45) NOT NULL,
  cuisine VARCHAR(45) NOT NULL,
  restaurant_price_range VARCHAR(45) NOT NULL,
  gst BOOLEAN NOT NULL,
  service_charge BOOLEAN NOT NULL,
  restaurant_img_path oid,
  PRIMARY KEY (restaurant_id),
  CONSTRAINT fk_Restaurant_Admin1
  FOREIGN KEY (admin_id)
   REFERENCES Admin (admin_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION
  );


CREATE TABLE IF NOT EXISTS Customer (
  email VARCHAR(100) NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  customer_password VARCHAR(64) NOT NULL,
  customer_contact_number VARCHAR(45) NOT NULL,
  loyalty_point INT NOT NULL,
  PRIMARY KEY (email));


CREATE TABLE IF NOT EXISTS Manager (
  manager_id VARCHAR(10) NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  manager_username VARCHAR(45) NOT NULL,
  manager_password VARCHAR(50) NOT NULL,
  PRIMARY KEY (manager_id));


CREATE TABLE IF NOT EXISTS Food (
  food_id VARCHAR(10) NOT NULL,
  food_name VARCHAR(45) NOT NULL,
  food_description VARCHAR(500) NOT NULL,
  food_img_path oid,
  PRIMARY KEY (food_id));


CREATE TABLE IF NOT EXISTS Menu (
  menu_id VARCHAR(10) NOT NULL,
  restaurant_id VARCHAR(10) NOT NULL,
  menu_name VARCHAR(45) NOT NULL,
  menu_creation_date DATE NOT NULL,
  PRIMARY KEY (menu_id),
  CONSTRAINT fk_Menu_Restaurant1
    FOREIGN KEY (restaurant_id)
    REFERENCES Restaurant (restaurant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Food_Category (
  food_category_id VARCHAR(10) NOT NULL,
  food_category_name VARCHAR(45) NOT NULL,
  food_category_img_path oid,
  PRIMARY KEY (food_category_id));


CREATE TABLE IF NOT EXISTS Sub_Category (
  sub_category_id VARCHAR(10) NOT NULL,
  food_category_id VARCHAR(10) NOT NULL,
  sub_category_name VARCHAR(45) NOT NULL,
  sub_category_img_path oid,
  PRIMARY KEY (sub_category_id),
  CONSTRAINT fk_Sub_Category_Food_Category1
    FOREIGN KEY (food_category_id)
    REFERENCES Food_Category (food_category_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Food_Price (
  menu_id VARCHAR(10) NOT NULL,
  food_id VARCHAR(10) NOT NULL,
  food_category_id VARCHAR(10) NOT NULL,
  sub_category_id VARCHAR(10) NOT NULL,
  food_price DOUBLE PRECISION NOT NULL,
  availability BOOLEAN NOT NULL,
  PRIMARY KEY (menu_id, food_id, food_category_id),
  CONSTRAINT fk_Menu_has_Food_Menu1
    FOREIGN KEY (menu_id)
    REFERENCES Menu (menu_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Menu_has_Food_Food1
    FOREIGN KEY (food_id)
    REFERENCES Food (food_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Food_Price_Food_Category1
    FOREIGN KEY (food_category_id)
    REFERENCES Food_Category (food_category_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Food_Price_Sub_Category1
    FOREIGN KEY (sub_category_id)
    REFERENCES Sub_Category (sub_category_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Seating_Table (
  qr_code VARCHAR(10) NOT NULL,
  restaurant_id VARCHAR(10) NOT NULL,
  table_number VARCHAR(10) NOT NULL,
  table_capacity INT NOT NULL,
  PRIMARY KEY (qr_code),
  CONSTRAINT fk_Seating_Table_Restaurant1
    FOREIGN KEY (restaurant_id)
    REFERENCES Restaurant (restaurant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Orders (
  order_id VARCHAR(10) NOT NULL,
  total_amount DOUBLE PRECISION NOT NULL,
  order_status VARCHAR(45) NOT NULL,
  payment_status VARCHAR(45) NOT NULL,
  mode_of_payment VARCHAR(45) NOT NULL,
  order_date TIMESTAMP NOT NULL,
  email VARCHAR(100) NOT NULL,
  qr_code VARCHAR(10) NOT NULL,
  token VARCHAR(100) NOT NULL,
  PRIMARY KEY (order_id),
  CONSTRAINT fk_Order_Customer1
  FOREIGN KEY (email)
    REFERENCES Customer (email)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Order_Seating_Table1
    FOREIGN KEY (qr_code)
    REFERENCES Seating_Table (qr_code)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Manager_Allocation (
  manager_id VARCHAR(10) NOT NULL,
  restaurant_id VARCHAR(10) NOT NULL,
  rights VARCHAR(45) NOT NULL,
  PRIMARY KEY (manager_id, restaurant_id),
  CONSTRAINT fk_Restaurant_has_Manager_Restaurant1
    FOREIGN KEY (restaurant_id)
    REFERENCES Restaurant (restaurant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Restaurant_has_Manager_Manager1
    FOREIGN KEY (manager_id)
    REFERENCES Manager (manager_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Customisation (
  customisation_id VARCHAR(10) NOT NULL,
  menu_id VARCHAR(10) NOT NULL,
  food_id VARCHAR(10) NOT NULL,
  food_category_id VARCHAR(10) NOT NULL,
  customisation_name VARCHAR(45) NOT NULL,
  PRIMARY KEY (customisation_id),
  CONSTRAINT fk_Food_Price_has_Customisation_Option_Food_Price1
    FOREIGN KEY (menu_id , food_id , food_category_id)
    REFERENCES Food_Price (menu_id , food_id , food_category_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Customisation_Option (
  customisation_option_id VARCHAR(10) NOT NULL,
  customisation_id VARCHAR(10) NOT NULL,
  option_description VARCHAR(500) NOT NULL,
  option_price DOUBLE PRECISION NOT NULL,
  PRIMARY KEY (customisation_option_id),
  CONSTRAINT fk_Customisation_Option_Customisation1
    FOREIGN KEY (customisation_id)
    REFERENCES Customisation (customisation_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Customer_Order (
  customer_order_id VARCHAR(10) NOT NULL,
  menu_id VARCHAR(10) NOT NULL,
  food_id VARCHAR(10) NOT NULL,
  food_category_id VARCHAR(10) NOT NULL,
  order_id VARCHAR(10) NOT NULL,
  customer_order_quantity INT NOT NULL,
  customer_order_price DOUBLE PRECISION NOT NULL,
  customer_order_remarks VARCHAR(500) NOT NULL,
  PRIMARY KEY (customer_order_id),
  CONSTRAINT fk_Customer_Order_Order1
    FOREIGN KEY (order_id)
    REFERENCES Orders (order_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Customer_Order_Food_Price1
    FOREIGN KEY (menu_id , food_id , food_category_id)
    REFERENCES Food_Price (menu_id , food_id , food_category_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Activity_Log (
  activity_log_id VARCHAR(10) NOT NULL,
  manager_id VARCHAR(10) NOT NULL,
  restaurant_id VARCHAR(10) NOT NULL,
  log_description VARCHAR(500) NOT NULL,
  log_change_timestamp TIMESTAMP NOT NULL,
  PRIMARY KEY (activity_log_id),
  CONSTRAINT fk_Activity_Log_Manager1
    FOREIGN KEY (manager_id)
    REFERENCES Manager (manager_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Activity_Log_Restaurant1
    FOREIGN KEY (restaurant_id)
    REFERENCES Restaurant (restaurant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS Customisation_Selected (
  customisation_option_id VARCHAR(10) NOT NULL,
  customer_order_id VARCHAR(10) NOT NULL,
  PRIMARY KEY (customisation_option_id, customer_order_id),
  CONSTRAINT fk_Customisation_Option_has_Customer_Order_Customisation_Opti1
    FOREIGN KEY (customisation_option_id)
    REFERENCES Customisation_Option (customisation_option_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Customisation_Selected_Customer_Order1
    FOREIGN KEY (customer_order_id)
    REFERENCES Customer_Order (customer_order_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
