-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 17. Feb 2022 um 18:02
-- Server-Version: 10.4.19-MariaDB
-- PHP-Version: 8.0.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `prog3`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `movies`
--

CREATE TABLE `movies` (
  `ID` int(11) NOT NULL,
  `Titel` varchar(300) NOT NULL,
  `Regisseure` varchar(300) NOT NULL,
  `Bewertung` tinyint(4) NOT NULL,
  `Genre` varchar(300) NOT NULL,
  `Laenge` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `movies`
--

INSERT INTO `movies` (`ID`, `Titel`, `Regisseure`, `Bewertung`, `Genre`, `Laenge`) VALUES
(39, 'Matrix Reloaded', 'Lana und Lilly Wachowski', 10, 'Action', 136),
(40, 'Rammstein: Paris', 'Jonas Åkerlund', 8, 'Musik', 98),
(41, 'Harry Potter und der Feuerkelch', 'Mike Newell', 6, 'Fantasy', 157);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `movies`
--
ALTER TABLE `movies`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
