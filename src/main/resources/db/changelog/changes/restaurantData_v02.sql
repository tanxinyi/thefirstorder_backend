INSERT INTO Restaurant
  (restaurant_id, admin_id, restaurant_name, restaurant_description, restaurant_contact_number, restaurant_opening_hours, building, street, postal_code, cuisine, restaurant_price_range, gst, service_charge, restaurant_img_path)
VALUES
  ('R001', 'AD001', 'Ajisen Ramen', 'Authentic Japanese food', '12345678', 'Weekdays: 11am - 9pm
			Weekends: 10am - 10pm', 'Changi City Point', '5 Changi Business Park Central 1, #01-35/36', '486038', 'Japanese', 'High', true, true, NULL),
  ('R002', 'AD001', 'NamNam Noodle Bar', 'Authentic Vietnamese food', '87654321', 'Weekdays: 11am - 9pm
			Weekends: 10am - 10pm', 'Raffles City Shopping Centre', '252 North Bridge Road, #B1-46/47', '201021', 'Vietnamese', 'Medium', true, true, NULL),
  ('R003', 'AD001', 'Nakhon Kitchen', 'Authentic Thai food', '62868785', 'Weekdays: 12pm - 9pm
			Weekends: 10am - 10pm', 'Residential Area', '212 Hougang Street 21, #01-341', '727272', 'Thai', 'Low', true, true, NULL),
  ('R004', 'AD001', 'The Daily Scoop', 'At The Daily Scoop, we live and breathe ice cream. Whether it’s dreaming up the next flavor, hand-churning up a batch of ice cream, or rolling out freshly baked cones.', '65094875', 'Weekdays: 12pm - 9pm
			Weekends: 10am - 10pm', 'SOTA', '1 Zubir Said Dr, #01-03 School Of The Arts', '227968', 'Dessert', 'Low', true, true, NULL),
  ('R005', 'AD001', 'B3', 'Burger. Beer. Bistro. ESTD 2016 By SMUAA', '64932264', 'Mon to Fri: 8:30am to 12am only', 'SMU School of Information System', '80 Stamford Road (SMU) #B1-61 Basement Concourse School of Information Systems', '178902', 'Western', 'Medium', true, false, NULL),
  ('R006', 'AD001', 'Din Tai Fung', 'Authentic Taiwanese food', '12345678', 'Weekdays: 11am - 9pm
			Weekends: 10am - 10pm', 'Nex', '23 Serangoon Central, #B1-10', '556083', 'Chinese', 'High', true, true, NULL),
  ('R007', 'AD001', 'Bricklane', 'Bricklane, your hidden home in the city.
            Be sure to drop by for solid mexican food and smooth drinks ', '82020404', 'Mon-Thu 11am–11pm
            Fri 11am–12am
            Sat 5pm–12am
            Sun Closed', 'Li Ka Shing Library', '70 Stamford Road #01-22', '178901', 'Western', 'Medium', false, false, NULL),
  ('R008', 'AD001', 'Yoogane', 'Cheesy Korean food', '12345678', 'Weekdays: 11am - 9pm
			Weekends: 10am - 10pm', 'WestGate', '3 Gateway Dr, #03-08 Westgate', '608532', 'Korean', 'High', true, true, NULL);