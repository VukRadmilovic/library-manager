CREATE TABLE posetilac
(
	posID integer NOT NULL,
	posIme varchar2(50) NOT NULL,
	posPrz varchar2(50) NOT NULL,
	brTel varchar(15),
	email varchar(50),
	stanje decimal(16, 2),
	CONSTRAINT posetilac_PK PRIMARY KEY (posID),
	CONSTRAINT posetilac_brTel_UQ UNIQUE (brTel),
	CONSTRAINT posetilac_email_UQ UNIQUE (email)
);
CREATE TABLE knjiga
(
	knjigaID integer NOT NULL,
	knjigaNaziv varchar2(50) NOT NULL,
	godIzdavanja integer,
	kratakOpis varchar2(255),
	brIzdanja integer,
	brStrana integer,
	CONSTRAINT knjiga_PK PRIMARY KEY (knjigaID),
	CONSTRAINT knjiga_brIzdanja_CH CHECK (brIzdanja>0),
	CONSTRAINT knjiga_brStrana_CH CHECK (brStrana>0)
);
CREATE TABLE donacijaKnjige
(
	donacijaID integer NOT NULL,
	brKnjiga integer NOT NULL,
	datDoniranja date,
	posID integer,
	knjigaID integer NOT NULL,
	CONSTRAINT donacijaKnjige_PK PRIMARY KEY (donacijaID),
	CONSTRAINT donacijaKnjige_pos_FK FOREIGN KEY (posID) REFERENCES posetilac (posID),
	CONSTRAINT donacijaKnjige_knjiga_FK FOREIGN KEY (knjigaID) REFERENCES knjiga (knjigaID),
	CONSTRAINT donacijaKnjige_CH CHECK (brKnjiga>0)
);
CREATE TABLE autor
(
	autorID integer NOT NULL,
	autorIme varchar2(50) NOT NULL,
	autorPrz varchar2(50) NOT NULL,
	kratkaBio varchar2(255) NOT NULL,
	CONSTRAINT autor_PK PRIMARY KEY (autorID)
);
CREATE TABLE autorKnjige
(
	autorID integer NOT NULL,
	knjigaID integer NOT NULL,
	CONSTRAINT autorKnjige_PK PRIMARY KEY (autorID, knjigaID),
	CONSTRAINT autorKnjige_autor_FK FOREIGN KEY (autorID) REFERENCES autor(autorID),
	CONSTRAINT autorKnjige_knjiga_FK FOREIGN KEY (knjigaID) REFERENCES knjiga(knjigaID)
);
CREATE TABLE zanr
(
	zanrID integer NOT NULL,
	zanrNaziv varchar2(20) NOT NULL,
	CONSTRAINT zanr_PK PRIMARY KEY (zanrID)
);
CREATE TABLE zanrKnjige
(
	zanrID integer NOT NULL,
	knjigaID integer NOT NULL,
	CONSTRAINT zanrKnjige_PK PRIMARY KEY (zanrID, knjigaID),
	CONSTRAINT zanrKnjige_zanr_FK FOREIGN KEY (zanrID) REFERENCES zanr(zanrID),
	CONSTRAINT zanrKnjige_knjiga_FK FOREIGN KEY (knjigaID) REFERENCES knjiga(knjigaID)
);