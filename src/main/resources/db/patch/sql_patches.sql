alter table ayahs  add FULLTEXT KEY `ayahs_FULLTEXT_index` (`textdesc`);
ALTER TABLE ayahs ADD column textdesc_normalized TEXT;

--- remove_accents function start --------
DROP FUNCTION IF EXISTS remove_accents;
DELIMITER //

create FUNCTION remove_accents (textdesc TEXT charset utf8mb4)
RETURNS TEXT charset utf8mb4
READS SQL DATA
DETERMINISTIC
BEGIN

   return replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace
	(replace(replace(replace(replace(textdesc, 'ىٰ','ى'), 'ٰ ',''), ' ۚ ',' '), 'ۗ ',' '), 'آ','ا'), 'آ','ا'), 'ٱ','ا'), 'أ','ا'), 'إ','ا'),'ا۟','ا'),'لآ','لا'), 'ؤ','و'), 'وٓ','و'), 'ٍ',''), 'ِ',''), 'ً',''), 'َ',''), 'ّ',''), 'ْ',''), 'ۖ',''), 'ٌ','')
    , 'ُ',''),' ۙ',' '),'  ',' ');

END; //

DELIMITER ;



--- remove_accents function end --------

alter table ayahs drop INDEX ayahs_FULLTEXT_index;
alter table ayahs add FULLTEXT KEY `ayahs_FULLTEXT_index` (`textdesc_normalized`);
update ayahs set textdesc_normalized = remove_accents(textdesc);


ALTER TABLE `lmswiam`.`user_custom`
CHANGE COLUMN `photo` `photo` LONGBLOB NULL DEFAULT NULL ;

ALTER TABLE `lmswiam`.`user_custom`
CHANGE COLUMN `biography` `biography` LONGTEXT NULL DEFAULT NULL ;

