SFTP TO DATABSE
CREATE TABLE orderdetail
(
whseid                 VARCHAR(30) NOT NULL,
storerkey              VARCHAR(20) NOT NULL,
orderkey               VARCHAR(100) NOT NULL,
orderlinenumber        VARCHAR(50) NOT NULL,
externlineno           INT,
sku                    VARCHAR(100) NOT NULL,
openqty                INT,
originalqty            INT,
uom                    VARCHAR(10),
newallocationstrategy  VARCHAR(50),
lottable01             VARCHAR(100),
lottable02             VARCHAR(100),
lottable03             VARCHAR(100),
lottable07             VARCHAR(100),
pickinginstructions      VARCHAR(255),

    CONSTRAINT PK_OrderDetail
        PRIMARY KEY (orderkey, orderlinenumber),

    CONSTRAINT FK_OrderDetail_Orders
        FOREIGN KEY (orderkey)
            REFERENCES orders(orderkey)
            ON DELETE CASCADE
);
GO