INSERT INTO tbl_meetingroom (id,name,description,available_from,available_until) VALUES (1,'Ocean','OceanMeetingRoom', '07:00:00','20:59:59');
INSERT INTO tbl_meetingroom (id,name,description,available_from,available_until) VALUES (2,'Mountain','MountainMeetingRoom', '07:00:00','20:59:59');
INSERT INTO tbl_meetingroom (id,name,description,available_from,available_until) VALUES (3,'Forest','ForestMeetingRoom', '07:00:00','20:59:59');

INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (1,'2021-01-23','10:00:00','11:59:00','Noe',1);
INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (2,'2021-01-23','12:00:00','12:59:00','Noe',1);
INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (3,'2021-01-23','10:00:00','11:59:00','Noe',2);
INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (4,'2021-01-23','12:00:00','11:59:00','Noe',2);
INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (5,'2021-01-23','10:00:00','11:59:00','Noe',3);
INSERT INTO tbl_reservation (id,reserved_date,reserved_from,reserved_until,reserved_for,room_id) VALUES (6,'2021-01-23','12:00:00','12:59:00','Noe',3);
