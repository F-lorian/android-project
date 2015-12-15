-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mar 15 Décembre 2015 à 02:49
-- Version du serveur :  5.6.21
-- Version de PHP :  5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `sign_project`
--

-- --------------------------------------------------------

--
-- Structure de la table `group`
--

CREATE TABLE IF NOT EXISTS `group` (
`id` int(11) NOT NULL,
  `name` varchar(500) NOT NULL,
  `type` enum('private','public') NOT NULL DEFAULT 'public',
  `creator` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `group`
--

INSERT INTO `group` (`id`, `name`, `type`, `creator`) VALUES
(2, 'test', 'public', 1);

-- --------------------------------------------------------

--
-- Structure de la table `signalement`
--

CREATE TABLE IF NOT EXISTS `signalement` (
`id` int(11) NOT NULL,
  `content` text NOT NULL,
  `note` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `diffusion` enum('user','group') DEFAULT NULL,
  `type` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `signalement`
--

INSERT INTO `signalement` (`id`, `content`, `note`, `date`, `diffusion`, `type`) VALUES
(1, 'test', 'test', '2015-12-08 21:25:20', 'group', 1),
(2, 'test2', 'test2', '2015-12-08 21:30:18', 'user', 1);

-- --------------------------------------------------------

--
-- Structure de la table `signalement_for_group`
--

CREATE TABLE IF NOT EXISTS `signalement_for_group` (
  `signalement` int(11) NOT NULL,
  `group` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `signalement_for_user`
--

CREATE TABLE IF NOT EXISTS `signalement_for_user` (
  `signalement` int(11) NOT NULL,
  `user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `signalement_for_user`
--

INSERT INTO `signalement_for_user` (`signalement`, `user`) VALUES
(2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `signalement_type`
--

CREATE TABLE IF NOT EXISTS `signalement_type` (
`id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `signalement_type`
--

INSERT INTO `signalement_type` (`id`, `name`) VALUES
(1, 'alert'),
(2, 'event');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `pseudo` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `gcm_regid` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`id`, `email`, `pseudo`, `password`, `gcm_regid`) VALUES
(1, 'test@test.com', 'test', 'test', '7548474675'),
(2, 'fghfgh', 'fghfgh', 'dfdfg', '87474858434');

-- --------------------------------------------------------

--
-- Structure de la table `user_in_group`
--

CREATE TABLE IF NOT EXISTS `user_in_group` (
  `user` int(11) NOT NULL,
  `group` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `group`
--
ALTER TABLE `group`
 ADD PRIMARY KEY (`id`), ADD KEY `creator` (`creator`);

--
-- Index pour la table `signalement`
--
ALTER TABLE `signalement`
 ADD PRIMARY KEY (`id`), ADD KEY `type` (`type`), ADD KEY `type_2` (`type`);

--
-- Index pour la table `signalement_for_group`
--
ALTER TABLE `signalement_for_group`
 ADD KEY `signalement` (`signalement`,`group`), ADD KEY `group` (`group`);

--
-- Index pour la table `signalement_for_user`
--
ALTER TABLE `signalement_for_user`
 ADD KEY `signalement` (`signalement`,`user`), ADD KEY `user` (`user`);

--
-- Index pour la table `signalement_type`
--
ALTER TABLE `signalement_type`
 ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `email` (`email`), ADD KEY `email_2` (`email`);

--
-- Index pour la table `user_in_group`
--
ALTER TABLE `user_in_group`
 ADD KEY `user` (`user`,`group`), ADD KEY `group` (`group`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `group`
--
ALTER TABLE `group`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `signalement`
--
ALTER TABLE `signalement`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `signalement_type`
--
ALTER TABLE `signalement_type`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `group`
--
ALTER TABLE `group`
ADD CONSTRAINT `fk_creator` FOREIGN KEY (`creator`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `signalement`
--
ALTER TABLE `signalement`
ADD CONSTRAINT `fk_type` FOREIGN KEY (`type`) REFERENCES `signalement_type` (`id`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Contraintes pour la table `signalement_for_group`
--
ALTER TABLE `signalement_for_group`
ADD CONSTRAINT `fk_link_group_signalement` FOREIGN KEY (`group`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_link_signalement_group` FOREIGN KEY (`signalement`) REFERENCES `signalement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `signalement_for_user`
--
ALTER TABLE `signalement_for_user`
ADD CONSTRAINT `fk_link_signalement_user` FOREIGN KEY (`signalement`) REFERENCES `signalement` (`id`),
ADD CONSTRAINT `fk_link_user_signalement` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `user_in_group`
--
ALTER TABLE `user_in_group`
ADD CONSTRAINT `fk_group` FOREIGN KEY (`group`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
