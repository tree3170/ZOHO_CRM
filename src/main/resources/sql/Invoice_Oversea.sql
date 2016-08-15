USE [Biz_Matrix00]
GO

/****** Object:  Table [dbo].[Invoice_Oversea]    Script Date: 08/02/2016 13:38:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Invoice_Oversea](
	[Invoice_OverseaID] [int] IDENTITY(1,1) NOT NULL,
	[VesselName] [nvarchar](50) NULL,
	[FromCityID] [int] NULL,
	[ToCityID] [int] NULL,
	[PlannedETD] [datetime] NULL,
	[PlannedETA] [datetime] NULL,
	[ActualETD] [datetime] NULL,
	[ActualETA] [datetime] NULL,
	[LCNo] [nvarchar](50) NULL,
	[IssuedBank] [nvarchar](300) NULL,
	[OrderNo] [nvarchar](50) NULL,
	[InvoiceID] [int] NULL,
 CONSTRAINT [PK_Invoice_Oversea] PRIMARY KEY CLUSTERED 
(
	[Invoice_OverseaID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO


