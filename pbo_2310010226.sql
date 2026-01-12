-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 12 Jan 2026 pada 16.25
-- Versi server: 10.4.28-MariaDB
-- Versi PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pbo_2310010226`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `kasir`
--

CREATE TABLE `kasir` (
  `id_kasir` varchar(20) NOT NULL,
  `level` varchar(10) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kasir`
--

INSERT INTO `kasir` (`id_kasir`, `level`, `nama`, `password`) VALUES
('1', 'Owner', 'admin', '123'),
('2', 'Pegawai', 'asbi', '123');

-- --------------------------------------------------------

--
-- Struktur dari tabel `konsumen`
--

CREATE TABLE `konsumen` (
  `id_konsumen` varchar(20) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `alamat` varchar(20) DEFAULT NULL,
  `telp` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `konsumen`
--

INSERT INTO `konsumen` (`id_konsumen`, `nama`, `alamat`, `telp`) VALUES
('1', 'Asbi', 'Handil Bakti', '08123'),
('2', 'Rizky', 'Kalteng', '08124');

-- --------------------------------------------------------

--
-- Struktur dari tabel `paket`
--

CREATE TABLE `paket` (
  `id_paket` varchar(15) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `harga` int(11) NOT NULL,
  `durasi` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `paket`
--

INSERT INTO `paket` (`id_paket`, `nama`, `harga`, `durasi`) VALUES
('1', 'Cuci Reguler 30 Menit', 15000, '30 Menit'),
('2', 'Cuci Reguler 60 Menit', 25000, '60 Menit'),
('3', 'Pengering 30 Menit', 10000, '30 Menit'),
('4', 'Cuci + Kering 60 Menit', 30000, '60 Menit'),
('5', 'Cuci + Kering Jumbo 90 Menit', 45000, '90 Menit');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id_pembayaran` varchar(20) NOT NULL,
  `jenis_pembayaran` varchar(30) NOT NULL,
  `tipe` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pembayaran`
--

INSERT INTO `pembayaran` (`id_pembayaran`, `jenis_pembayaran`, `tipe`) VALUES
('1', 'Dana', 'Non Tunai'),
('2', 'OVO', 'Non Tunai'),
('3', 'GoPay', 'Non Tunai'),
('4', 'Cash', 'Tunai');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` varchar(20) NOT NULL,
  `kasir` varchar(20) NOT NULL,
  `tgl_transaksi` varchar(20) NOT NULL,
  `konsumen` varchar(20) NOT NULL,
  `paket` varchar(15) NOT NULL,
  `keterangan` varchar(100) DEFAULT NULL,
  `pembayaran` varchar(20) NOT NULL,
  `durasi` varchar(10) DEFAULT NULL,
  `total` varchar(11) NOT NULL,
  `tgl_ambil` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `kasir`, `tgl_transaksi`, `konsumen`, `paket`, `keterangan`, `pembayaran`, `durasi`, `total`, `tgl_ambil`) VALUES
('1', '2', '1 Januari 2026', '2', '1', 'Mesin 1', '1', '30 Menit', '15000', '1 Januari 2026'),
('2', '1', '2', '2', '2', '2', '1', '60 Menit', '25000', '2');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `kasir`
--
ALTER TABLE `kasir`
  ADD PRIMARY KEY (`id_kasir`);

--
-- Indeks untuk tabel `konsumen`
--
ALTER TABLE `konsumen`
  ADD PRIMARY KEY (`id_konsumen`);

--
-- Indeks untuk tabel `paket`
--
ALTER TABLE `paket`
  ADD PRIMARY KEY (`id_paket`);

--
-- Indeks untuk tabel `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`);

--
-- Indeks untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `fk_transaksi_kasir` (`kasir`),
  ADD KEY `fk_transaksi_konsumen` (`konsumen`),
  ADD KEY `fk_transaksi_paket` (`paket`),
  ADD KEY `fk_transaksi_pembayaran` (`pembayaran`);

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `fk_transaksi_kasir` FOREIGN KEY (`kasir`) REFERENCES `kasir` (`id_kasir`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_konsumen` FOREIGN KEY (`konsumen`) REFERENCES `konsumen` (`id_konsumen`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_paket` FOREIGN KEY (`paket`) REFERENCES `paket` (`id_paket`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaksi_pembayaran` FOREIGN KEY (`pembayaran`) REFERENCES `pembayaran` (`id_pembayaran`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
