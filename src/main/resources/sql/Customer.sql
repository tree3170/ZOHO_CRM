USE [Biz_Matrix00]
GO

/****** Object:  Table [dbo].[Customer]    Script Date: 08/02/2016 13:34:44 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Customer](
	[CustomerID] [int] IDENTITY(1,1) NOT NULL,
	[CustomerRef] [nvarchar](50) NULL,
	[Name] [nvarchar](500) NULL,
	[City] [nvarchar](50) NULL,
	[State] [nvarchar](50) NULL,
	[CountryID] [int] NULL,
	[Direct] [nvarchar](50) NULL,
	[Website] [nvarchar](500) NULL,
	[Tel] [nvarchar](50) NULL,
	[Fax] [nvarchar](50) NULL,
	[Email] [nvarchar](50) NULL,
	[DeliveryAddress] [nvarchar](2000) NULL,
	[MailAddress] [nvarchar](2000) NULL,
	[CurrencyName] [nvarchar](5) NULL,
	[Contact] [nvarchar](50) NULL,
	[Remark] [nvarchar](4000) NULL,
	[PostNo] [nvarchar](50) NULL,
	[DeliveryMethod] [nvarchar](50) NULL,
	[Discount] [decimal](18, 4) NULL,
	[CreditLimit] [money] NULL,
	[Enabled] [bit] NULL,
	[LatestEditBy] [nvarchar](50) NULL,
	[BankAccount] [nvarchar](50) NULL,
	[PaymentTermID] [int] NULL,
	[PaymentMethodDesc] [nvarchar](50) NULL,
	[Class] [int] NULL,
	[BlockingOffset] [int] NULL,
	[LatestEditTime] [datetime] NULL,
	[StaffID] [int] NULL,
	[CreationTime] [datetime] NULL,
	[CashAccountID] [int] NULL,
	[CompanyType] [nvarchar](50) NULL,
	[ARAccountID] [int] NULL,
	[APAccountID] [int] NULL,
	[SDAccountID] [int] NULL,
	[PDAccountID] [int] NULL,
 CONSTRAINT [PK_Customer] PRIMARY KEY CLUSTERED 
(
	[CustomerID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO


