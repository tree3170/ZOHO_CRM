USE [Biz_Matrix00]
GO

/****** Object:  Table [dbo].[Item]    Script Date: 08/02/2016 13:37:37 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[Item](
	[ItemID] [int] IDENTITY(1,1) NOT NULL,
	[ItemRef] [nvarchar](50) NULL,
	[OldItemRef] [nvarchar](50) NULL,
	[Enabled] [bit] NULL,
	[Name] [nvarchar](50) NULL,
	[Description] [nvarchar](2000) NULL,
	[Catagory] [nvarchar](50) NULL,
	[SubCategory] [nvarchar](50) NULL,
	[Property1] [nvarchar](50) NULL,
	[Property2] [nvarchar](50) NULL,
	[Property3] [nvarchar](50) NULL,
	[Remark] [nvarchar](2000) NULL,
	[LatestEditBy] [nvarchar](50) NULL,
	[Unit] [nvarchar](10) NULL,
	[DiscountBy] [int] NULL,
	[MinInventory] [decimal](18, 4) NULL,
	[DefaultWarehouse] [int] NULL,
	[ItemType] [nvarchar](5) NULL,
	[LatestEditTime] [datetime] NULL,
	[Barcode] [nvarchar](50) NULL,
	[LinkFilePath] [nvarchar](2000) NULL,
	[ACSalesID] [int] NULL,
	[ACCostOfSalesID] [int] NULL,
	[ACInventoryID] [int] NULL,
	[ACInvAdjID] [int] NULL,
	[ACLendBorrowID] [int] NULL,
	[IsPriceByMarkup] [bit] NULL,
	[IsMarkupFromCal] [bit] NULL,
	[CostingMethod] [nvarchar](50) NULL,
	[UnitCostFixed] [decimal](18, 8) NULL,
	[RateA] [decimal](18, 4) NULL,
	[ExtraA] [decimal](18, 4) NULL,
	[OuterLength] [decimal](18, 4) NULL,
	[OuterWidth] [decimal](18, 4) NULL,
	[OuterHeight] [decimal](18, 4) NULL,
	[QtyPerOuter] [decimal](18, 4) NULL,
	[OuterGrossWeight] [decimal](18, 4) NULL,
	[OuterNetWeight] [decimal](18, 4) NULL,
	[PackageWeightUnit] [nvarchar](10) NULL,
	[PackageSizeUnit] [nvarchar](10) NULL,
	[QtyPerInner] [decimal](18, 4) NULL,
	[SalesTaxCodeID] [int] NULL,
	[PurchaseTaxCodeID] [int] NULL,
	[LastUnitCost] [decimal](18, 8) NULL,
	[PreferedVenderID] [int] NULL,
	[UNC] [varchar](300) NULL,
	[URL] [varchar](300) NULL,
	[ExtraCost] [decimal](18, 8) NULL,
	[IsNew] [bit] NULL,
	[Packing] [nvarchar](500) NULL,
	[ProductSize] [nvarchar](50) NULL,
	[ProdLeadTime] [int] NULL,
	[SampleLeadTime] [int] NULL,
	[SampleCharge] [nvarchar](50) NULL,
	[BlockCharge] [nvarchar](50) NULL,
	[ExtraCost2] [nvarchar](50) NULL,
	[UNC0] [nvarchar](300) NULL,
	[UNC1] [nvarchar](300) NULL,
	[UNC2] [nvarchar](300) NULL,
	[UNC3] [nvarchar](300) NULL,
	[UNC4] [nvarchar](300) NULL,
	[URL0] [nvarchar](300) NULL,
	[URL1] [nvarchar](300) NULL,
	[URL2] [nvarchar](300) NULL,
	[URL3] [nvarchar](300) NULL,
	[URL4] [nvarchar](300) NULL,
	[FtyQty1] [int] NULL,
	[FtyQty2] [int] NULL,
	[FtyQty3] [int] NULL,
	[FtyQty4] [int] NULL,
	[FtyQty5] [int] NULL,
	[FtyCurrency] [nvarchar](50) NULL,
	[ListQty1] [int] NULL,
	[ListQty2] [int] NULL,
	[ListQty3] [int] NULL,
	[ListQty4] [int] NULL,
	[ListQty5] [int] NULL,
	[ListCurrency] [nvarchar](50) NULL,
 CONSTRAINT [PK_Item] PRIMARY KEY CLUSTERED 
(
	[ItemID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


