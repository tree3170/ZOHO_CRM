USE [Biz_Matrix00]
GO

/****** Object:  Table [dbo].[Invoice]    Script Date: 08/02/2016 13:33:22 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Invoice](
	[InvoiceID] [int] IDENTITY(1,1) NOT NULL,
	[InvoiceRef] [nvarchar](50) NULL,
	[InvoiceDate] [datetime] NULL,
	[SORef] [nvarchar](1000) NULL,
	[CustomerID] [int] NULL,
	[CusRef] [nvarchar](50) NULL,
	[CusName] [nvarchar](500) NULL,
	[CusMailAddress] [nvarchar](500) NULL,
	[CusDeliveryAddress] [nvarchar](500) NULL,
	[CusTel] [nvarchar](50) NULL,
	[CusFax] [nvarchar](50) NULL,
	[CusEmail] [nvarchar](50) NULL,
	[CusContact] [nvarchar](50) NULL,
	[CusDiscount] [decimal](18, 8) NULL,
	[CustomerPONo] [nvarchar](50) NULL,
	[RemarkBin] [image] NULL,
	[LatestEditBy] [nvarchar](50) NULL,
	[PaymentTermID] [int] NULL,
	[FreightAmount] [money] NULL,
	[OtherCharge] [money] NULL,
	[Deposit] [money] NULL,
	[Total] [decimal](18, 8) NULL,
	[CurrencyName] [nvarchar](50) NULL,
	[ExchangeRate] [decimal](18, 8) NULL,
	[PayMethod] [nvarchar](50) NULL,
	[DeliveryMethod] [nvarchar](50) NULL,
	[DNNo] [nvarchar](200) NULL,
	[InvoiceType] [nvarchar](50) NULL,
	[AccountJournalID] [int] NULL,
	[ProjectID] [int] NULL,
	[StaffName] [nvarchar](50) NULL,
	[StaffID] [int] NULL,
	[CreatorUserID] [int] NULL,
	[LatestEditTime] [datetime] NULL,
 CONSTRAINT [PK_Invoice] PRIMARY KEY CLUSTERED 
(
	[InvoiceID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO


