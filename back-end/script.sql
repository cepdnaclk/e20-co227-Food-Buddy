USE foodbuddy2;

INSERT INTO `role`(role_name)
VALUES
('ROLE_USER'),
('ROLE_SELLER'),
('ROLE_ADMIN');

-- password - password
INSERT INTO app_user(email, password, username)
VALUES
-- sellers
('alice@example.com', '$2a$10$GkeGb8wyJ/inHo2YP1/j4eY/fTILPy.7HGvOttTPnaa56pkBTuNcC', 'alice123'),
('bob@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'bobby'),
('carol@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'carol456'),
('dave@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'dave789'),
('eve@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'eve101'),
-- users
('frank@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'frank_the_tank'),
('grace@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'gracie'),
('henry@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'henry789'),
('irene@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'irene_999'),
-- admins
('jack@example.com', '$2a$04$n8ZTlLLifWMMR9ChwsfsmeIZSGNub/e6Rf.fh/0NhspMXb.PHXHRG', 'jack_sparrow');

-- id 1,2,3,4,5 - sellers (ROLE_USER + ROLE_SELLER)
-- id 6,7,8, 9 - users (ROLE_USER)
-- id 10 - admins (ROLE_USER + ROLE_SELLER + ROLE_ADMIN)
INSERT INTO user_role(user_id, role_id)
VALUES
(1,1),(2,1),(3,1),(4,1),(5,1),
(1,2),(2,2),(3,2),(4,2),(5,2),

(6,1),(7,1),(8,1),(9,1),

(10,1),(10,2),(10,3);

INSERT INTO address (address_line1, address_line2, address_line3)
VALUES
('Colombo Road', 'Kiribathkumbura', 'peradeniya 20442'),
('7H87+9P6', 'Embilmeegame', 'Pilimathalawa'),
('No. 24', 'Sri Dalada Veediya', 'Kandy 20000'),
('22 F', 'Mahiyangane', 'Padiyatalawa Hwy'),
('454/1E', 'New Kandy Rd', '11650'),
('22 F', 'abc road', 'Kandy');

-- my location = (7.261444320940439, 80.57678163526387)

-- kapila bakers = (7.267722191447389, 80.57599708914255) , 0.738 km, ('Colombo Road', 'Kiribathkumbura', 'peradeniya 20442')
-- Jinadasa Thalaguli = (7.265920921192404, 80.56427315377036), 1.47 km, ('7H87+9P6', 'Embilmeegame', 'Pilimathalawa')
-- Delight bakers kandy = (7.293179992033037, 80.63675783930738), 7.5 km, ('No. 24', 'Sri Dalada Veediya', 'Kandy 20000')
-- Salalihini hotel (7.284044469763235, 80.78349377788307), 22.94 km, ('Kandy', 'Mahiyangane', 'Padiyatalawa Hwy')
-- Yaman (6.9499108184465905, 79.99332976063563) 73.11 km, ('454/1E', 'New Kandy Rd', '11650')

INSERT INTO shop (shop_name, latitude, longitude, address_id, owner_id, phone_number, image)
VALUES
('kapila bakers', 7.267722191447389, 80.57599708914255, 1, 1, '0771234567','kapila_bakers.png'), -- 0.738 km
('Jinadasa Thalaguli', 7.265920921192404, 80.56427315377036, 2, 2, '0772345678', 'jinadasa_thalaguli.png'), -- 1.47 km
('Delight bakers kandy', 7.293179992033037, 80.63675783930738, 3, 3, '0773456789', 'delight.png'), -- 7.5 km
('Salalihini hotel', 7.284044469763235, 80.78349377788307, 4, 4, '0774567890', 'salalihini.png'), -- 22.94 km
('Yaman Bakers',6.9499108184465905, 79.99332976063563, 5, 5, '0775678901', 'default.jpg'), -- 73.11 km
('admin shop',6.9499108184465905, 79.99332976063563, 6, 10, '0775678901', 'default.jpg');

INSERT INTO category(category_name, image)
VALUES
-- 1
('Rice & Curry', "category-rice-and-curry.jpg"), -- 1
('Bakery', "category-bakery.jpg"), -- 2
('Vegetables', "category-vege.jpeg"), -- 3
('Fruits', "fruits-category.jpg"), -- 4
('Beverages', "category-beverages.jpg"), -- 5
('Indian', "category-indian.png"), -- 6
('Desserts', "category-dessert.jpg"), -- 7
('Fast Food', "category-fast-food.jpg"), -- 8
('Chinese', "category-chinese.jpg"), -- 9
('other', ""); -- 10

INSERT INTO product
(category_id, shop_id, product_name, original_price, discounted_price, discount_percentage, quantity, valid_until, description, image)
VALUES
(1, 1, 'Egg Rice & Curry', 450, 300, 33, 10, '2024-07-31 23:59:59.000000', "Savor our rich and flavorful Egg Rice & Curry, a perfect blend of spices and hearty goodness.", 'egg-rice-and-curry.png'),
(1, 1, 'Vegetable Rice & Curry', 250, 180, 28, 12, '2024-07-31 23:59:59.000000', "Enjoy a healthy and delicious Vegetable Rice & Curry, packed with fresh, vibrant veggies.", 'vegetable-rice-and-curry.jpg'),
(1, 2, 'Egg Rice & Curry', 300, 200, 33, 8, '2024-07-31 23:59:59.000000', "Savor our rich and flavorful Egg Rice & Curry, a perfect blend of spices and hearty goodness.", 'egg-rice-and-curry-2.jpg'),

(9, 2, 'Chinese Set Menu - Vege', 650, 500, 23, 15, '2024-07-31 23:59:59.000000', "Indulge in our Chinese Set Menu - Vege, a delightful combination of traditional flavors and vegetarian delight.", 'chinese-set menu-vege.png'),

(2, 3, 'Bread', 150, 80, 47, 18, '2024-07-31 23:59:59.000000', "Freshly baked Bread, perfect for any meal or a quick snack.", 'bread.jpeg'),
(2, 3, 'Fish Bun', 100, 70, 30, 15, '2024-07-31 23:59:59.000000', "Taste the ocean with our savory Fish Bun, filled with a deliciously seasoned fish filling.", 'fish-bun.jpg'),
(2, 4, 'Vegetable Bun', 80, 50, 38, 16, '2024-07-31 23:59:59.000000', "Our Vegetable Bun is a wholesome treat, bursting with fresh, flavorful veggies.", 'vegetable-bun.jpg'),

(6, 4, 'Plain Dosai', 300, 200, 33, 13, '2024-07-31 23:59:59.000000', "Experience the authentic taste of South India with our crispy, golden Plain Dosai.", 'plain-dosai.jpg'),
(6, 4, 'Naan', 500, 300, 40, 6, '2024-07-31 23:59:59.000000', "Soft and fluffy Naan, a perfect accompaniment to any curry or dish.", 'naan.jpg')

-- (5, 4, 'Chicken Burger', 980, 700, 28, 8, '2024-07-31 23:59:59.000000', "Sink your teeth into our juicy Chicken Burger, a mouthwatering delight with every bite.", 'chicken-burger.jpg')
;
