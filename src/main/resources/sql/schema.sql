DROP TABLE if EXISTS item_cart;
DROP TABLE if EXISTS item_pedido;
DROP TABLE if EXISTS pedido;
DROP TABLE if EXISTS usuario;
DROP TABLE if EXISTS info_pedido;
DROP TABLE if EXISTS cart;
DROP TABLE if EXISTS product;

DROP SEQUENCE if EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START WITH 500 INCREMENT BY 1;

CREATE TABLE cart (id bigint NOT NULL, PRIMARY KEY (id));
CREATE TABLE info_pedido (id bigint NOT NULL, card VARCHAR(255), city VARCHAR(255), country VARCHAR(255), email VARCHAR(255), line1 VARCHAR(255), line2 VARCHAR(255), name VARCHAR(255), phone VARCHAR(255), postal_code VARCHAR(255), province VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE item_cart (id bigint NOT NULL, quantity INTEGER NOT NULL, fk_product bigint, fk_cart bigint, PRIMARY KEY (id));
CREATE TABLE item_pedido (id bigint NOT NULL, quantity integer NOT NULL, unit_sales_price DECIMAL(19,2), fk_product bigint, fk_pedido bigint, PRIMARY KEY (id));
CREATE TABLE pedido (id bigint NOT NULL, date TIMESTAMP, status VARCHAR(255), fk_info_pedido bigint, fk_user bigint, PRIMARY KEY (id));
CREATE TABLE product (id bigint NOT NULL, category VARCHAR(255), description VARCHAR(255), price DECIMAL(19,2), stock integer NOT NULL, title VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE usuario (id bigint NOT NULL, email VARCHAR(255), password VARCHAR(255), role VARCHAR(255), fk_cart bigint, PRIMARY KEY (id));


ALTER TABLE item_cart ADD CONSTRAINT fk_productIdOnItemCart FOREIGN KEY (fk_product) REFERENCES product(id);
ALTER TABLE item_cart ADD CONSTRAINT fk_cartIdOnItemCart FOREIGN KEY (fk_cart) REFERENCES cart(id);
ALTER TABLE item_pedido ADD CONSTRAINT fk_productIdOnItemPedido FOREIGN KEY (fk_product) REFERENCES product(id);
ALTER TABLE item_pedido ADD CONSTRAINT fk_pedidoIdOnItemPedido FOREIGN KEY (fk_pedido) REFERENCES pedido(id);
ALTER TABLE pedido ADD CONSTRAINT fk_info_pedidoIdOnPedido FOREIGN KEY (fk_info_pedido) REFERENCES info_pedido(id);
ALTER TABLE pedido ADD CONSTRAINT fk_userIdOnPedido FOREIGN KEY (fk_user) REFERENCES usuario(id);
ALTER TABLE usuario ADD CONSTRAINT fk_cartIdOnUser FOREIGN KEY (fk_cart) REFERENCES cart(id);
