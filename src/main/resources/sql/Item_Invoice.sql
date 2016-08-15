USE [Biz_Matrix00]
GO

/****** Object:  Table [dbo].[Item_Invoice]    Script Date: 08/02/2016 13:41:11 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Item_Invoice](
	[Item_InvoiceID] [int] IDENTITY(1,1) NOT NULL,
	[SOID] [int] NULL,
	[Item_SOID] [int] NULL,
	[InvoiceID] [int] NULL,
	[ItemID] [int] NULL,
	[ItemType] [nvarchar](5) NULL,
	[ItemRef] [nvarchar](50) NULL,
	[ItemName] [nvarchar](50) NULL,
	[Description] [nvarchar](2000) NULL,
	[PackingQty] [decimal](18, 4) NULL,
	[PackingUnit] [nvarchar](50) NULL,
	[EqualBasicUnitQty] [decimal](18, 4) NULL,
	[InvoicePrice] [decimal](18, 8) NULL,
	[Unit] [nvarchar](50) NULL,
	[Quantity] [decimal](18, 4) NULL,
	[ItemDiscount] [decimal](18, 4) NULL,
	[ItemTax] [decimal](18, 4) NULL,
	[StockTakeRecord] [nvarchar](4000) NULL,
	[StockUnitCost] [decimal](18, 8) NULL,
	[CusItemNo] [nvarchar](50) NULL,
	[OrderNo] [nvarchar](50) NULL,
 CONSTRAINT [PK_Item_Invoice] PRIMARY KEY CLUSTERED 
(
	[Item_InvoiceID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO


