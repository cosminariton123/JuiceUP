CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	salt VARCHAR(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
	trust_score DECIMAL(7,4) NOT NULL,
	car_max_km_range INT NOT NULL,
	guarded_place_preference INT NOT NULL,
	nr_of_parking_spots_preference INT NOT NULL,
	type2_preference INT NOT NULL,
	wall_preference INT NOT NULL,
	supercharger_preference INT NOT NULL,
	min_kwh_preference INT NOT NULL,
	min_rating_preference INT NOT NULL,
	min_trust_preference INT NOT NULL,
	how_many_people_voted INT NOT NULL
);

email, password, salt, last_name, first_name, car_max_km_range, guarded_place_preference, nr_of_parking_posts_preference, type2_preference, wall_preference, supercharger_preference, min_kwh_preference, trust_score

use juiceupdatabase;


--INSERT INTO users(email, password, salt, last_name, first_name, trust_score)
--VALUES 
--('dinu_miron@a.com', 'parola1', 'salt', 'Dinu', 'Miron', 70),
--('marius_mitica@a.com', 'parola2', 'salt' ,'Marius', 'Mitica', 90),
--('bogdan_turture@a.com', 'parola3', 'salt' ,'Bogdan', 'Turture', 30)



SELECT * FROM users;

DROP TABLE users;

CREATE TABLE chargingstations(
	id INT IDENTITY(1,1) PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	set_by_user VARCHAR(255) FOREIGN KEY REFERENCES users(email) NOT NULL,
	x_coordinate DECIMAL(6,4) NOT NULL,
	y_coordinate DECIMAL (7,4) NOT NULL,
	guarded INT NOT NULL,
	parking_number_of_places INT NOT NULL,
	type2 INT NOT NULL,
	wall INT NOT NULL,
	supercharger INT NOT NULL,
	outputkwh INT NOT NULL,
	rating DECIMAL(5,4) NOT NULL,
	how_many_people_rated INT NOT NULL,
	CONSTRAINT UNIQUELOCATION UNIQUE (x_coordinate, y_coordinate)
);

SELECT trust_score FROM chargingstations ch JOIN users us ON(ch.set_by_user = us.email);


INSERT INTO chargingstations(name, set_by_user  ,x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh)
VALUES ('Electric Castle', 'cosmin@a.com', 44.4291, 26.0875, 1, 20, 1, 1, 0, 50);

INSERT INTO chargingstations(name, set_by_user  ,x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh)
VALUES ('Electric Castle', 'cosmin@a.com', 44.4291, 26.0875, 1, 20, 1, 1, 0, 50);


SELECT * FROM chargingstations;

SELECT * FROM chargingstations WHERE x_coordinate LIKE 7.3626 AND y_coordinate LIKE 38.7890;

DROP TABLE chargingstations;




CREATE TABLE distances(
	id_from INT,
	id_to INT,
	road_distance INT NOT NULL,
	CONSTRAINT id_from_to PRIMARY KEY (id_from, id_to),
	CONSTRAINT FK_from FOREIGN KEY (id_from) REFERENCES chargingstations(id),
	CONSTRAINT FK_to FOREIGN KEY (id_to) REFERENCES chargingstations(id)
);

SELECT * FROM distances;

CREATE TABLE votes(
	email_who VARCHAR(255),
	id_station INT,
	CONSTRAINT vote PRIMARY KEY (email_who, id_station),
	CONSTRAINT FK_email_who FOREIGN KEY (email_who) REFERENCES users(email),
	CONSTRAINT FK_id_station FOREIGN KEY (id_station) REFERENCES chargingstations(id)
);

SELECT * FROM votes;



DROP TABLE distances;

DROP TABLE distances, chargingstations;

DROP TABLE distances, chargingstations, users, votes;


SELECT trust_score, how_many_people_voted FROM users WHERE email LIKE 'cosmin@a.com';