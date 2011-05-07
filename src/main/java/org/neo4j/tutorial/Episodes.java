package org.neo4j.tutorial;

import static org.neo4j.tutorial.EpisodeBuilder.episode;

import org.neo4j.graphdb.Transaction;

public class Episodes {

    private final DoctorWhoUniverse universe;

    public Episodes(DoctorWhoUniverse universe) {
        this.universe = universe;
    }

    public void insert() {
        Transaction tx = universe.getDatabase().beginTx();
        try {
            season01();
            season02();
            season03();
            season04();
            season05();
            season06();
            season07();
            season08();
            season09();
            season10();
            season11();
            season12();
            season13();
            season14();
            season15();
            season16();
            season17();
            season18();
            season19();
            season20();
            episode(129).title("The Five Doctors").doctor("Richard Hurndall").doctor("William Hartnell").doctor("Patrick Troughton").doctor("Jon Pertwee").doctor("Tom Baker").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough", "Susan Foreman", "Sarah Jane Smith", "Romana").enemy("Master").fact(universe);
            season21();
            season22();
            season23();
            season24();
            season25();
            season26();
            episode(156).title("Doctor Who").doctor("Paul McGann").doctor("Sylvester McCoy").companion("Grace Holloway").enemy("Master").fact(universe);
            series01();
            episode(167).title("The Christmas Invasion").doctor("David Tennant").companion("Rose Tyler").enemySpecies("Sycorax").fact(universe);
            series02();
            episode(178).title("The Runaway Bride").doctor("David Tennant").companion("Donna Noble").enemy("Empress of Racnoss").fact(universe);
            series03();
            episode(188).title("Voyage of the Damned").doctor("David Tennant").companion("Astrid Peth").fact(universe);
            series04();
            series05();
            episode(213).title("A Christmas Carol").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").enemy("Kazran Sardick").fact(universe);
            series06();

            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void series06() {
        episode(214).title("The Impossible Astronaut").doctor("Matt Smith").companion("Amy Pond", "Rory Williams", "River Song").enemySpecies("Silence").fact(universe);
        episode(214).title("Day of the Moon").doctor("Matt Smith").companion("Amy Pond", "Rory Williams", "River Song").enemySpecies("Silence").fact(universe);
    }

    private void series05() {
        episode(199).title("The Next Doctor").doctor("David Tennant").companion("Jackson Lake", "Rosita Farisi").enemy("Miss Hartigan").enemySpecies("Cyberman").fact(universe);
        episode(200).title("Planet of the Dead").doctor("David Tennant").companion("Lady Christina de Souza").fact(universe);
        episode(201).title("The Waters of Mars").doctor("David Tennant").companion("Adelaide Brooke").fact(universe);
        episode(202).title("The End of Time").doctor("David Tennant").doctor("Matt Smith").companion("Wilfred Mott").enemy("Master", "Lord President").fact(universe);
        episode(203).title("The Eleventh Hour").doctor("Matt Smith").companion("Amy Pond").enemy("Prisoner Zero").fact(universe);
        episode(204).title("The Beast Below").doctor("Matt Smith").companion("Amy Pond").enemy("Prisoner Zero").fact(universe);
        episode(206).title("Victory of the Daleks").doctor("Matt Smith").companion("Amy Pond").enemySpecies("Dalek").fact(universe);
        episode(206).title("The Time of Angels").doctor("Matt Smith").companion("Amy Pond").enemySpecies("Weeping Angel").fact(universe);
        episode(206).title("Flesh and Stone").doctor("Matt Smith").companion("Amy Pond").enemySpecies("Weeping Angel").fact(universe);
        episode(207).title("The Vampires of Venice").doctor("Matt Smith").companion("Amy Pond").enemy("Signora Calvierri").fact(universe);
        episode(208).title("Amy's Choice").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").enemySpecies("Eknodine").fact(universe);
        episode(209).title("The Hungry Earth").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").enemySpecies("Silurian").fact(universe);
        episode(209).title("Cold Blood").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").enemySpecies("Silurian").fact(universe);
        episode(210).title("Vincent and the Doctor").doctor("Matt Smith").companion("Amy Pond").fact(universe);
        episode(211).title("The Lodger").doctor("Matt Smith").companion("Amy Pond").fact(universe);
        episode(212).title("The Pandorica Opens").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").enemySpecies("Dalek", "Auton", "Cyberman", "Sontaran", "Judoon", "Sycorax", "Hoix", "Silurian", "Roboform").fact(universe);
        episode(212).title("The Big Bang").doctor("Matt Smith").companion("Amy Pond", "Rory Williams").fact(universe);
    }

    private void series04() {
        episode(189).title("Partners in Crime").doctor("David Tennant").companion("Donna Noble").enemy("Miss Foster").fact(universe);
        episode(190).title("The Fires of Pompeii").doctor("David Tennant").companion("Donna Noble").enemy("Pyrovile").fact(universe);
        episode(191).title("Planet of the Ood").doctor("David Tennant").companion("Donna Noble").fact(universe);
        episode(192).title("The Sontaran Stratagem").doctor("David Tennant").companion("Donna Noble", "Martha Jones").enemy("General Staal").fact(universe);
        episode(192).title("The Poison Sky").doctor("David Tennant").companion("Donna Noble", "Martha Jones").enemy("General Staal").fact(universe);
        episode(193).title("The Doctor's Daughter").doctor("David Tennant").companion("Donna Noble", "Martha Jones").enemy("General Cobb").fact(universe);
        episode(194).title("The Unicorn and the Wasp").doctor("David Tennant").companion("Donna Noble").fact(universe);
        episode(195).title("Silence in the Library").doctor("David Tennant").companion("Donna Noble").enemySpecies("Vashta Nerada").fact(universe);
        episode(195).title("Forest of the Dead").doctor("David Tennant").companion("Donna Noble").enemySpecies("Vashta Nerada").fact(universe);
        episode(196).title("Midnight").doctor("David Tennant").companion("Donna Noble").fact(universe);
        episode(197).title("Turn Left").doctor("David Tennant").companion("Donna Noble", "Rose Tyler").fact(universe);
        episode(198).title("The Stolen Earth").doctor("David Tennant").companion("Donna Noble", "Rose Tyler", "Martha Jones", "Jack Harkness", "Sarah Jane Smith").enemySpecies("Dalek").fact(universe);
        episode(198).title("Journey's End").doctor("David Tennant").companion("Donna Noble", "Rose Tyler", "Martha Jones", "Jack Harkness", "Sarah Jane Smith").enemySpecies("Dalek").fact(universe);
    }

    private void series03() {
        episode(179).title("Smith and Jones").doctor("David Tennant").companion("Martha Jones").enemySpecies("Plasmavore").fact(universe);
        episode(180).title("The Shakespeare Code").doctor("David Tennant").companion("Martha Jones").enemySpecies("Carrionite").fact(universe);
        episode(181).title("Gridlock").doctor("David Tennant").companion("Martha Jones").enemySpecies("Macra").fact(universe);
        episode(182).title("Daleks in Manhattan").doctor("David Tennant").companion("Martha Jones").enemySpecies("Dalek").fact(universe);
        episode(182).title("Evolution of the Daleks").doctor("David Tennant").companion("Martha Jones").enemySpecies("Dalek").fact(universe);
        episode(183).title("The Lazarus Experiment").doctor("David Tennant").companion("Martha Jones").enemy("Lazarus").fact(universe);
        episode(184).title("42").doctor("David Tennant").companion("Martha Jones").fact(universe);
        episode(185).title("Human Nature").doctor("David Tennant").companion("Martha Jones").enemy("Family of Blood").fact(universe);
        episode(185).title("Family of Blood").doctor("David Tennant").companion("Martha Jones").enemy("Family of Blood").fact(universe);
        episode(186).title("Blink").doctor("David Tennant").companion("Martha Jones").enemySpecies("Weeping Angel").fact(universe);
        episode(187).title("Utopia").doctor("David Tennant").companion("Martha Jones", "Jack Harkness").enemy("Master").fact(universe);
        episode(187).title("The Sound of Drums").doctor("David Tennant").companion("Martha Jones", "Jack Harkness").enemy("Master").fact(universe);
        episode(187).title("Last of the Time Lords").doctor("David Tennant").companion("Martha Jones", "Jack Harkness").enemy("Master").fact(universe);
    }

    private void series02() {
        episode(168).title("New Earth").doctor("David Tennant").companion("Rose Tyler").fact(universe);
        episode(169).title("Tooth and Claw").doctor("David Tennant").companion("Rose Tyler").fact(universe);
        episode(170).title("School Reunion").doctor("David Tennant").companion("Rose Tyler", "Mickey Smith", "Sarah Jane Smith").enemySpecies("Krillitane").fact(universe);
        episode(171).title("The Girl in the Fireplace").doctor("David Tennant").companion("Rose Tyler", "Mickey Smith").enemySpecies("Clockwork Android").fact(universe);
        episode(172).title("Rise of the Cybermen").doctor("David Tennant").companion("Rose Tyler", "Mickey Smith").enemySpecies("Cyberman").fact(universe);
        episode(172).title("The Age of Steel").doctor("David Tennant").companion("Rose Tyler", "Mickey Smith").enemySpecies("Cyberman").fact(universe);
        episode(173).title("The Idiot's Lantern").doctor("David Tennant").companion("Rose Tyler").enemy("The Wire").fact(universe);
        episode(174).title("The Impossible Planet").doctor("David Tennant").companion("Rose Tyler").enemy("Beast").fact(universe);
        episode(174).title("The Satan Pit").doctor("David Tennant").companion("Rose Tyler").enemy("Beast").fact(universe);
        episode(175).title("Love & Monsters").doctor("David Tennant").companion("Rose Tyler").enemy("Abzorbaloff").fact(universe);
        episode(176).title("Fear Her").doctor("David Tennant").companion("Rose Tyler").fact(universe);
        episode(177).title("Army of Ghosts").doctor("David Tennant").companion("Rose Tyler").enemySpecies("Cyberman", "Dalek").fact(universe);
        episode(177).title("Doomsday").doctor("David Tennant").companion("Rose Tyler").enemySpecies("Cyberman", "Dalek").fact(universe);
    }

    private void series01() {
        episode(157).title("Rose").doctor("Christopher Eccleston").companion("Rose Tyler").enemySpecies("Auton").fact(universe);
        episode(158).title("The End of the World").doctor("Christopher Eccleston").companion("Rose Tyler").enemy("Cassandra").fact(universe);
        episode(159).title("The Unquiet Dead").doctor("Christopher Eccleston").companion("Rose Tyler").enemy("Gabriel Sneed").fact(universe);
        episode(160).title("Aliens of London").doctor("Christopher Eccleston").companion("Rose Tyler").enemySpecies("Slitheen").fact(universe);
        episode(160).title("World War Three").doctor("Christopher Eccleston").companion("Rose Tyler").enemySpecies("Slitheen").fact(universe);
        episode(161).title("Dalek").doctor("Christopher Eccleston").companion("Rose Tyler").enemySpecies("Dalek").fact(universe);
        episode(162).title("The Long Game").doctor("Christopher Eccleston").enemy("The Editor").companion("Rose Tyler").fact(universe);
        episode(163).title("Father's Day").doctor("Christopher Eccleston").companion("Rose Tyler").fact(universe);
        episode(164).title("The Empty Child").doctor("Christopher Eccleston").companion("Rose Tyler", "Jack Harkness").fact(universe);
        episode(164).title("The Doctor Dances").doctor("Christopher Eccleston").companion("Rose Tyler", "Jack Harkness").fact(universe);
        episode(165).title("Boom Town").doctor("Christopher Eccleston").companion("Rose Tyler", "Jack Harkness").enemySpecies("Slitheen").fact(universe);
        episode(166).title("Bad Wolf").doctor("Christopher Eccleston").companion("Rose Tyler", "Jack Harkness").enemySpecies("Dalek").fact(universe);
        episode(166).title("The Parting of the Ways").doctor("Christopher Eccleston").doctor("David Tennant").companion("Rose Tyler", "Jack Harkness").enemySpecies("Dalek").fact(universe);
    }

    private void season26() {
        episode(151).title("Battlefield").doctor("Sylvester McCoy").companion("Ace").fact(universe);
        episode(152).title("Ghost Light").doctor("Sylvester McCoy").companion("Ace").enemy("Josiah Samuel Smith").fact(universe);
        episode(153).title("The Curse of Fenric").doctor("Sylvester McCoy").companion("Ace").enemy("Fenric").fact(universe);
        episode(154).title("Survival").doctor("Sylvester McCoy").companion("Ace").enemy("Master").fact(universe);
    }

    private void season25() {
        episode(148).title("Remembrance of the Daleks").doctor("Sylvester McCoy").companion("Ace").enemy("Davros").enemySpecies("Dalek").fact(universe);
        episode(149).title("The Happiness Patrol").doctor("Sylvester McCoy").companion("Ace").enemy("Helen A").fact(universe);
        episode(150).title("Silver Nemesis").doctor("Sylvester McCoy").companion("Ace").enemySpecies("Cyberman").fact(universe);
        episode(151).title("The Greatest Show in the Galaxy").doctor("Sylvester McCoy").companion("Ace").fact(universe);
    }

    private void season24() {
        episode(144).title("Time and the Rani").doctor("Colin Baker").doctor("Sylvester McCoy").companion("Melanie Bush").enemy("Rani").fact(universe);
        episode(145).title("Paradise Towers").doctor("Sylvester McCoy").companion("Melanie Bush").enemy("Kroagnon").fact(universe);
        episode(146).title("Delta and the Bannermen").doctor("Sylvester McCoy").companion("Melanie Bush").enemy("Gavrok").fact(universe);
        episode(147).title("Dragonfire").doctor("Sylvester McCoy").companion("Melanie Bush", "Ace").enemy("Kane").fact(universe);
    }

    private void season23() {
        episode(143).title("The Mysterious Planet").doctor("Colin Baker").companion("Peri Brown").fact(universe);
        episode(143).title("Mindwarp").doctor("Colin Baker").companion("Peri Brown").fact(universe);
        episode(143).title("Terror of the Vervoids").doctor("Colin Baker").companion("Melanie Bush").fact(universe);
        episode(143).title("The Ultimate Foe").doctor("Colin Baker").companion("Melanie Bush").enemy("Master").fact(universe);
    }

    private void season22() {
        episode(136).title("The Twin Dilemma").doctor("Colin Baker").companion("Peri Brown").fact(universe);
        episode(137).title("Attack of the Cybermen").doctor("Colin Baker").companion("Peri Brown").fact(universe);
        episode(138).title("Vengeance on Varos").doctor("Colin Baker").companion("Peri Brown").fact(universe);
        episode(139).title("The Mark of the Rani").doctor("Colin Baker").companion("Peri Brown").enemy("Master", "Rani").fact(universe);
        episode(140).title("The Two Doctors").doctor("Colin Baker").doctor("Patrick Troughton").companion("Peri Brown", "Jamie McCrimmon").enemy("Shockeye", "Chessene", "Dastari").fact(universe);
        episode(141).title("Timelash").doctor("Colin Baker").companion("Peri Brown").enemy("Borad").fact(universe);
        episode(142).title("Revelation of the Daleks").doctor("Colin Baker").companion("Peri Brown").enemySpecies("Dalek").fact(universe);
    }

    private void season21() {
        episode(130).title("Warriors of the Deep").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough").enemySpecies("Silurian", "Sea Devil").fact(universe);
        episode(131).title("The Awakening").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough").enemy("Malus").fact(universe);
        episode(132).title("Frontios").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough").enemySpecies("Tractator").fact(universe);
        episode(133).title("Resurrection of the Daleks").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough").enemySpecies("Dalek").fact(universe);
        episode(134).title("Planet of Fire").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough", "Peri Brown").enemy("Master").fact(universe);
        episode(135).title("The Caves of Androzani").doctor("Peter Davison").doctor("Colin Baker").companion("Peri Brown").enemy("Master").fact(universe);
    }

    private void season20() {
        episode(123).title("Arc of Infinity").doctor("Peter Davison").companion("Nyssa", "Tegan Jovanka").enemy("Omega").fact(universe);
        episode(124).title("Snakedance").doctor("Peter Davison").companion("Nyssa", "Tegan Jovanka").enemy("Mara").fact(universe);
        episode(125).title("Mawdryn Undead").doctor("Peter Davison").companion("Nyssa", "Tegan Jovanka", "Vislor Turlough").enemy("Mawdryn", "Black Guardian").fact(universe);
        episode(126).title("Terminus").doctor("Peter Davison").companion("Nyssa", "Tegan Jovanka", "Vislor Turlough").enemy("Vanir").fact(universe);
        episode(127).title("Enlightenment").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough").enemy("Black Guardian").fact(universe);
        episode(128).title("The King's Demons").doctor("Peter Davison").companion("Tegan Jovanka", "Vislor Turlough", "Kamelion").enemy("Master").fact(universe);
    }

    private void season19() {
        episode(116).title("Castrovalva").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").enemy("Master").fact(universe);
        episode(117).title("Four to Doomsday").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").fact(universe);
        episode(118).title("Kinda").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").enemy("Mara").fact(universe);
        episode(119).title("The Visitation").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").enemy("Terileptils").fact(universe);
        episode(120).title("Black Orchid").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").fact(universe);
        episode(121).title("Earthshock").doctor("Peter Davison").companion("Adric","Nyssa", "Tegan Jovanka").enemySpecies("Cyberman").fact(universe);
        episode(122).title("Time-Flight").doctor("Peter Davison").companion("Nyssa", "Tegan Jovanka").enemy("Master").fact(universe);
    }

    private void season18() {
        episode(109).title("The Leisure Hive").doctor("Tom Baker").companion("Romana", "K9").fact(universe);
        episode(110).title("Meglos").doctor("Tom Baker").companion("Romana", "K9").enemy("Meglos").fact(universe);
        episode(111).title("Full Circle").doctor("Tom Baker").companion("Romana", "K9", "Adric").enemySpecies("Marshman").fact(universe);
        episode(112).title("State of Decay").doctor("Tom Baker").companion("Romana", "K9", "Adric").enemy("Zargo", "Camilla", "Aukon").fact(universe);
        episode(113).title("Warriors' Gate").doctor("Tom Baker").companion("Romana", "K9", "Adric").fact(universe);
        episode(114).title("The Keeper of Traken").doctor("Tom Baker").companion("Adric").enemy("Master").fact(universe);
        episode(115).title("Logopolis").doctor("Tom Baker").companion("Adric","Nyssa", "Tegan Jovanka").enemy("Master").fact(universe);
    }

    private void season17() {
        episode(104).title("Destiny of the Daleks").doctor("Tom Baker").companion("Romana").enemySpecies("Dalek").fact(universe);
        episode(105).title("City of Death").doctor("Tom Baker").companion("Romana").enemy("Scaroth").fact(universe);
        episode(106).title("The Creature from the Pit").doctor("Tom Baker").companion("Romana", "K9").enemy("Erato", "Lady Adrasta").fact(universe);
        episode(107).title("Nightmare of Eden").doctor("Tom Baker").companion("Romana", "K9").enemySpecies("Mandrel").fact(universe);
        episode(108).title("The Horns of Nimon").doctor("Tom Baker").companion("Romana", "K9").enemySpecies("Nimon").fact(universe);
    }

    private void season16() {
        episode(98).title("The Ribos Operation").doctor("Tom Baker").companion("Romana", "K9").enemy("Graff Vynda-K", "Black Guardian").fact(universe);
        episode(99).title("The Pirate Planet").doctor("Tom Baker").companion("Romana", "K9").enemy("Pirate Captain").fact(universe);
        episode(100).title("The Stones of Blood").doctor("Tom Baker").companion("Romana", "K9").enemy("de Vries").enemySpecies("Ogri").fact(universe);
        episode(101).title("The Androids of Tara").doctor("Tom Baker").companion("Romana", "K9").enemy("Count Grendel of Gracht").fact(universe);
        episode(102).title("The Power of Kroll").doctor("Tom Baker").companion("Romana").enemy("Kroll").fact(universe);
        episode(103).title("The Armageddon Factor").doctor("Tom Baker").companion("Romana", "K9").enemy("The Shadow", "Black Guardian").fact(universe);
    }

    private void season15() {
        episode(92).title("Horror of Fang Rock").doctor("Tom Baker").companion("Leela").fact(universe);
        episode(93).title("The Invisible Enemy").doctor("Tom Baker").companion("Leela", "K9").enemy("Nucleus").fact(universe);
        episode(94).title("Image of the Fendahl").doctor("Tom Baker").companion("Leela").enemy("Fendahl").fact(universe);
        episode(95).title("The Sun Makers").doctor("Tom Baker").companion("Leela", "K9").enemy("Collector").fact(universe);
        episode(96).title("Underworld").doctor("Tom Baker").companion("Leela", "K9").enemy("Oracle").fact(universe);
        episode(97).title("The Invasion of Time").doctor("Tom Baker").companion("Leela", "K9").enemySpecies("Sontaran").enemy("Stor").fact(universe);
    }

    private void season14() {
        episode(86).title("The Masque of Mandragora").doctor("Tom Baker").companion("Sarah Jane Smith").enemy("Count Federico", "Captain Rossini").fact(universe);
        episode(87).title("The Hand of Fear").doctor("Tom Baker").companion("Sarah Jane Smith").enemy("Eldrad").fact(universe);
        episode(88).title("The Deadly Assassin").doctor("Tom Baker").enemy("Master").fact(universe);
        episode(89).title("The Face of Evil").doctor("Tom Baker").companion("Leela").enemy("Xoanon").fact(universe);
        episode(90).title("The Robots of Death").doctor("Tom Baker").companion("Leela").fact(universe);
        episode(91).title("The Talons of Weng-Chiang").doctor("Tom Baker").companion("Leela").enemy("Li H'sen Chang").fact(universe);
    }

    private void season13() {
        episode(80).title("Terror of the Zygons").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").enemy("Skarasen").enemySpecies("Zygon").fact(universe);
        episode(81).title("Planet of Evil").doctor("Tom Baker").companion("Sarah Jane Smith").fact(universe);
        episode(82).title("Pyramids of Mars").doctor("Tom Baker").companion("Sarah Jane Smith").enemy("Sutekh").fact(universe);
        episode(83).title("The Android Invasion").doctor("Tom Baker").companion("Sarah Jane Smith").enemySpecies("Android").fact(universe);
        episode(84).title("The Brain of Morbius").doctor("Tom Baker").companion("Sarah Jane Smith").enemy("Morbius", "Doctor Solon").fact(universe);
        episode(85).title("The Seeds of Doom").doctor("Tom Baker").companion("Sarah Jane Smith").enemy("Harrison Chase").fact(universe);
    }

    private void season12() {
        episode(75).title("Robot").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").enemy("K1 Robot").fact(universe);
        episode(76).title("The Ark in Space").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").fact(universe);
        episode(77).title("The Sontaran Experiment").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").enemy("Styre").fact(universe);
        episode(78).title("Genesis of the Daleks").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").enemy("Davros").enemySpecies("Dalek").fact(universe);
        episode(79).title("Revenge of the Cybermen").doctor("Tom Baker").companion("Sarah Jane Smith", "Harry Sullivan").enemy("Cyberleader").enemySpecies("Cyberman").fact(universe);
    }

    private void season11() {
        episode(70).title("The Time Warrior").doctor("Jon Pertwee").companion("Sarah Jane Smith").enemy("Linx").fact(universe);
        episode(71).title("Invasion of the Dinosaurs").doctor("Jon Pertwee").companion("Sarah Jane Smith").fact(universe);
        episode(72).title("Death to the Daleks").doctor("Jon Pertwee").companion("Sarah Jane Smith").enemySpecies("Dalek").fact(universe);
        episode(73).title("The Monster of Peladon").doctor("Jon Pertwee").companion("Sarah Jane Smith").enemy("Chancellor Ortron").fact(universe);
        episode(74).title("Planet of the Spiders").doctor("Jon Pertwee").doctor("Tom Baker").companion("Sarah Jane Smith").fact(universe);
    }

    private void season10() {
        episode(65).title("The Three Doctors").doctor("Jon Pertwee").doctor("Patrick Troughton").doctor("William Hartnell").companion("Jo Grant").enemy("Omega").fact(universe);
        episode(66).title("Carnival of Monsters").doctor("Jon Pertwee").companion("Jo Grant").fact(universe);
        episode(67).title("Frontier in Space").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").fact(universe);
        episode(68).title("Planet of the Daleks").doctor("Jon Pertwee").companion("Jo Grant").enemySpecies("Dalek").fact(universe);
        episode(69).title("The Green Death").doctor("Jon Pertwee").companion("Jo Grant").enemy("BOSS").fact(universe);
    }

    private void season09() {
        episode(60).title("Day of the Daleks").doctor("Jon Pertwee").companion("Jo Grant").enemy("Bok", "Master").fact(universe);
        episode(61).title("The Curse of Peladon").doctor("Jon Pertwee").companion("Jo Grant").fact(universe);
        episode(62).title("The Sea Devils").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").enemySpecies("Sea Devil").fact(universe);
        episode(63).title("The Mutants").doctor("Jon Pertwee").enemy("The Marshal").companion("Jo Grant").fact(universe);
        episode(64).title("The Time Monster").doctor("Jon Pertwee").enemy("Master").companion("Jo Grant").fact(universe);
    }

    private void season08() {
        episode(55).title("Terror of the Autons").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").enemySpecies("Auton").fact(universe);
        episode(56).title("The Mind of Evil").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").fact(universe);
        episode(57).title("The Claws of Axos").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").enemySpecies("Axon").fact(universe);
        episode(58).title("Colony in Space").doctor("Jon Pertwee").companion("Jo Grant").enemy("Master").fact(universe);
        episode(59).title("The Daemons").doctor("Jon Pertwee").companion("Jo Grant").enemy("Bok", "Master").fact(universe);
    }

    private void season07() {
        episode(51).title("Spearhead from Space").doctor("Jon Pertwee").companion("Liz Shaw").enemySpecies("Auton").fact(universe);
        episode(52).title("Doctor Who and the Silurians").doctor("Jon Pertwee").companion("Liz Shaw").enemySpecies("Silurian").fact(universe);
        episode(53).title("The Ambassadors of Death").doctor("Jon Pertwee").companion("Liz Shaw").enemy("Reegan").fact(universe);
        episode(54).title("Inferno").doctor("Jon Pertwee").companion("Liz Shaw").fact(universe);
    }

    private void season06() {
        episode(44).title("The Dominators").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemySpecies("Dominator", "Quark").fact(universe);
        episode(45).title("The Mind Robber").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemy("Master").fact(universe);
        episode(46).title("The Invasion").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").fact(universe);
        episode(47).title("The Krotons").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemySpecies("Kroton").fact(universe);
        episode(48).title("The Seeds of Death").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemySpecies("Ice Warrior").fact(universe);
        episode(49).title("The Space Pirates").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemy("Caven", "Dervish").fact(universe);
        episode(50).title("The War Games").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Zoe Heriot").enemy("War Chief").fact(universe);
    }

    private void season05() {
        episode(37).title("The Tomb of the Cybermen").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").enemySpecies("Cyberman").fact(universe);
        episode(38).title("The Abominable Snowmen").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").fact(universe);
        episode(39).title("The Ice Warriors").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").enemySpecies("Ice Warrior").fact(universe);
        episode(40).title("The Enemy of the World").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").fact(universe);
        episode(41).title("The Web of Fear").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").fact(universe);
        episode(42).title("Fury from the Deep").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").fact(universe);
        episode(43).title("The Wheel in Space").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").enemySpecies("Cyberman").fact(universe);
    }

    private void season04() {
        episode(30).title("The Power of the Daleks").doctor("Patrick Troughton").companion("Polly", "Ben Jackson").enemySpecies("Dalek").fact(universe);
        episode(31).title("The Highlanders").doctor("Patrick Troughton").companion("Polly", "Ben Jackson", "Jamie McCrimmon").enemySpecies("Dalek").fact(universe);
        episode(32).title("The Underwater Menace").doctor("Patrick Troughton").companion("Polly", "Ben Jackson", "Jamie McCrimmon").enemy("Zaroff").fact(universe);
        episode(33).title("The Moonbase").doctor("Patrick Troughton").companion("Polly", "Ben Jackson", "Jamie McCrimmon").enemySpecies("Cyberman").fact(universe);
        episode(34).title("The Macra Terror").doctor("Patrick Troughton").companion("Polly", "Ben Jackson", "Jamie McCrimmon").enemySpecies("Macra").fact(universe);
        episode(35).title("The Faceless Ones").doctor("Patrick Troughton").companion("Polly", "Ben Jackson", "Jamie McCrimmon").fact(universe);
        episode(36).title("The Evil of the Daleks").doctor("Patrick Troughton").companion("Jamie McCrimmon", "Victoria Waterfield").enemySpecies("Dalek").fact(universe);
    }

    private void season03() {
        episode(18).title("Galaxy 4").doctor("William Hartnell").companion("Vicki", "Steven Taylor").enemySpecies("Drahvin").fact(universe);
        episode(19).title("Mission to the Unknown").fact(universe);
        episode(20).title("The Myth Makers").doctor("William Hartnell").companion("Vicki", "Steven Taylor", "Katarina").fact(universe);
        episode(21).title("The Daleks' Master Plan").doctor("William Hartnell").companion("Steven Taylor", "Katarina", "Sara Kingdom").enemySpecies("Dalek").fact(universe);
        episode(22).title("The Massacre of St Bartholomew's Eve").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor").fact(universe);
        episode(23).title("The Ark").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor").fact(universe);            
        episode(24).title("The Celestial Toymaker").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor").enemy("The Toymaker").fact(universe);
        episode(25).title("The Gunfighters").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor").fact(universe);
        episode(26).title("The Savages").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor").fact(universe);
        episode(27).title("The War Machines").doctor("William Hartnell").companion("Dodo Chaplet", "Steven Taylor", "Polly").enemy("WOTAN").fact(universe);
        episode(28).title("The Smugglers").doctor("William Hartnell").companion("Polly", "Ben Jackson").fact(universe);
        episode(29).title("The Tenth Planet").doctor("William Hartnell").doctor("Patrick Troughton").companion("Polly", "Ben Jackson").fact(universe);
    }

    private void season02() {
        episode(9).title("Planet of Giants").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(10).title("The Dalek Invasion of Earth").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(11).title("The Rescue").doctor("William Hartnell").companion("Vicki", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(12).title("The Romans").doctor("William Hartnell").companion("Vicki", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(13).title("The Web Planet").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(14).title("The Crusade").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(15).title("The Space Museum").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(16).title("The Chase").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright", "Steven Taylor").enemySpecies("Dalek").fact(universe);
        episode(17).title("The Time Meddler").doctor("William Hartnell").companion("Vicki", "Steven Taylor").fact(universe);
    }

    private void season01() {
        episode(1).title("An Unearthly Child").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Stone Age Tribe").fact(universe);
        episode(2).title("The Daleks").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Dalek").fact(universe);
        episode(3).title("The Edge of Destruction").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(4).title("Marco Polo").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tegana").fact(universe);
        episode(5).title("The Keys of Marinus").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Yartek").fact(universe);
        episode(6).title("The Aztecs").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tlotoxl").fact(universe);
        episode(7).title("The Sensorites").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Sensorite").fact(universe);
        episode(8).title("The Reign of Terror").doctor("William Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Robespierre").enemy("Napoleon").fact(universe);
    }

}
