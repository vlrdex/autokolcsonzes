-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 30, 2025 at 04:42 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `autokolcson`
--

-- --------------------------------------------------------

--
-- Table structure for table `car`
--

CREATE TABLE `car` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(256) NOT NULL,
  `price_per_day` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- Dumping data for table `car`
--

INSERT INTO `car` (`id`, `name`, `price_per_day`, `active`) VALUES
(1, 'Opel Astra', 5800, 1),
(2, 'Ford Focus', 6200, 1),
(3, 'Toyota Corolla', 7000, 1),
(4, 'Volkswagen Golf', 6800, 1),
(5, 'Honda Civic', 7500, 1),
(6, 'Mazda 3', 6400, 1),
(7, 'Skoda Octavia', 6600, 1),
(8, 'Renault Clio', 5400, 1),
(9, 'Peugeot 308', 5600, 1),
(10, 'Hyundai i30', 6000, 1),
(11, 'Kia Ceed', 5900, 1),
(12, 'Seat Leon', 6300, 1),
(13, 'Fiat Tipo', 5200, 0),
(14, 'Nissan Qashqai', 8800, 1),
(15, 'BMW 3 Series', 12000, 1),
(16, 'Mercedes A-Class', 13500, 1);

-- --------------------------------------------------------

--
-- Table structure for table `rental`
--

CREATE TABLE `rental` (
  `id` int(10) UNSIGNED NOT NULL,
  `carid` int(10) UNSIGNED NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `name` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `address` varchar(400) NOT NULL,
  `phone` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_hungarian_ci;

--
-- Dumping data for table `rental`
--

INSERT INTO `rental` (`id`, `carid`, `start`, `end`, `name`, `email`, `address`, `phone`) VALUES
(1, 1, '2025-09-10', '2025-09-15', 'John Doe', 'john.doe@example.com', 'Budapest, Main St 12', '+36301234567'),
(2, 2, '2025-09-01', '2025-09-05', 'Anna Kovacs', 'anna.kovacs@example.com', 'Debrecen, Petofi u. 8', '+36207654321'),
(3, 3, '2025-09-03', '2025-09-08', 'Peter Nagy', 'peter.nagy@example.com', 'Szeged, Kossuth Lajos ter 4', '+36702345678'),
(4, 4, '2025-09-15', '2025-09-20', 'Maria Kiss', 'maria.kiss@example.com', 'Pecs, Arpad u. 22', '+36308765432'),
(5, 5, '2025-09-01', '2025-09-10', 'David Horvath', 'david.horvath@example.com', 'Miskolc, Bartok Bela u. 19', '+36201122334'),
(6, 6, '2025-09-05', '2025-09-12', 'Erika Toth', 'erika.toth@example.com', 'Gyor, Szechenyi ter 1', '+36709988776'),
(7, 7, '2025-10-02', '2025-10-06', 'Laszlo Balogh', 'laszlo.balogh@example.com', 'Sopron, Rakoczi u. 14', '+36304456677'),
(8, 8, '2025-09-15', '2025-09-22', 'Kata Varga', 'kata.varga@example.com', 'Kecskemet, Katona Jozsef ter 9', '+36205567788'),
(9, 9, '2025-10-01', '2025-10-07', 'Zoltan Farkas', 'zoltan.farkas@example.com', 'Nyiregyhaza, Szent Istvan u. 45', '+36706678899'),
(10, 10, '2025-08-10', '2025-08-15', 'Eva Molnar', 'eva.molnar@example.com', 'Szombathely, Fo ter 3', '+36309983344'),
(11, 11, '2025-09-24', '2025-10-01', 'Tamas Szabo', 'tamas.szabo@example.com', 'Eger, Dobo ter 6', '+36204452233'),
(12, 12, '2025-10-15', '2025-10-20', 'Zsuzsa Nemeth', 'zsuzsa.nemeth@example.com', 'Veszprem, Kossuth u. 11', '+36706672211'),
(13, 13, '2025-08-01', '2025-08-05', 'Miklós Olah', 'miklos.olah@example.com', 'Tatabanya, Varoshaz ter 18', '+36302234455'),
(14, 14, '2025-08-10', '2025-08-15', 'Agnes Barta', 'agnes.barta@example.com', 'Kaposvar, Petofi ter 7', '+36206678899'),
(15, 15, '2025-09-01', '2025-09-09', 'Janos Fekete', 'janos.fekete@example.com', 'Szolnok, Tiszaliget 2', '+36708891122'),
(16, 16, '2025-09-12', '2025-09-18', 'Edit Hajdu', 'edit.hajdu@example.com', 'Bekescsaba, Szechenyi u. 10', '+36305567788');

--
-- Triggers `rental`
--
DELIMITER $$
CREATE TRIGGER `check_rental_overlap` BEFORE INSERT ON `rental` FOR EACH ROW BEGIN
    DECLARE overlap_count INT;

    SELECT COUNT(*)
    INTO overlap_count
    FROM rental r
    WHERE r.carid = NEW.carid
      AND (
            NEW.start BETWEEN r.start AND r.end
         OR NEW.end BETWEEN r.start AND r.end
         OR r.start BETWEEN NEW.start AND NEW.end
         OR r.end BETWEEN NEW.start AND NEW.end
      );

    IF overlap_count > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ez az autó a megadott időszakban már foglalt!';
    END IF;
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rental`
--
ALTER TABLE `rental`
  ADD PRIMARY KEY (`id`),
  ADD KEY `autoid_fk` (`carid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `car`
--
ALTER TABLE `car`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `rental`
--
ALTER TABLE `rental`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rental`
--
ALTER TABLE `rental`
  ADD CONSTRAINT `autoid_fk` FOREIGN KEY (`carid`) REFERENCES `car` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
