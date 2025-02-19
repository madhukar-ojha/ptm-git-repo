ALTER TABLE menu_command
ADD COLUMN url VARCHAR(50) 
GENERATED ALWAYS AS (CONCAT(menuKey,'/',commandKey)) STORED;
COMMIT

ALTER TABLE menu_command
CHANGE COLUMN `url` `url` VARCHAR(50) 
GENERATED ALWAYS AS (CONCAT(menuKey,'/',commandKey)) STORED;
COMMIT

ALTER TABLE designation
CHANGE COLUMN createdBy  
GENERATED ALWAYS AS (date(SYSDATE())) STORED;

ALTER TABLE `employee`
CHANGE COLUMN `displayName` `displayName` VARCHAR(50)  
GENERATED ALWAYS AS (CONCAT(firstName,' ',lastName)) STORED;

ALTER TABLE menu_command
CHANGE COLUMN urlKey urlKey VARCHAR(50)
GENERATED ALWAYS AS (CONCAT(menuKey,'.',commandKey)) STORED;