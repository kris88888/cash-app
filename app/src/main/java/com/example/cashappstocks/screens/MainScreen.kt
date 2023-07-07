package com.example.cashappstocks.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cashappstocks.network.data.Stock
import java.util.Date

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    stocks: List<Stock>,
    convertDateFn: (Long) -> String
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(items = stocks,) { index, stock ->
            StockRow(stock = stock, convertDateFn)

            if (index != stocks.size-1) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Black)
                )

            }
        }
    }
}

@Composable
fun StockRow(stock: Stock, convertDateFn: (Long) -> String) {
    val constraintSet = ConstraintSet {
        val name = createRefFor("stock_name")
        val currentPriceLabel = createRefFor("price_label")
        val currentPriceValue = createRefFor("current_price_value")
        val quantityLabel = createRefFor("quantity_label")
        val quantityValue = createRefFor("quantity_value")
        val lastUpdate = createRefFor("last_updated_ts")

        val verticalGuideline = createGuidelineFromStart(fraction = 0.5f)
        constrain(name) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }

        constrain(currentPriceLabel) {
            top.linkTo(name.bottom)
            start.linkTo(parent.start)
            end.linkTo(verticalGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(currentPriceValue) {
            top.linkTo(currentPriceLabel.top)
            start.linkTo(verticalGuideline)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(quantityLabel) {
            top.linkTo(currentPriceLabel.bottom)
            baseline.linkTo(quantityValue.baseline)
            start.linkTo(parent.start)
            end.linkTo(verticalGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(quantityValue) {
            top.linkTo(currentPriceValue.bottom)
            start.linkTo(verticalGuideline)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(lastUpdate) {
            top.linkTo(quantityLabel.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
    }
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                MaterialTheme.colorScheme.background,
            )
            .padding(12.dp)
    ) {

        Text(
            text = "${stock.name} (${stock.ticker})",
            modifier = Modifier.layoutId("stock_name"),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Current Price",
            modifier = Modifier.layoutId("price_label"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${stock.current_price_cents} (${stock.currency})",
            modifier = Modifier.layoutId("current_price_value"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Quantity",
            modifier = Modifier.layoutId("quantity_label"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${stock.quantity ?: "NA"}",
            modifier = Modifier.layoutId("quantity_value"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "As of ${convertDateFn(stock.current_price_timestamp)}",
            modifier = Modifier
                .layoutId("last_updated_ts")
                .padding(top = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun PreviewStockRow() {
    StockRow(
        stock = Stock(
            "DJI",
            name = "Dow Jones Industrial Average",
            current_price_cents = "738884884848484848848484848",
            currency = "CAD",
            quantity = 8000,
            current_price_timestamp = Date().time
        ),
        convertDateFn = {
            "Jan 1 , 2023"
        }
    )
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(
        stocks = listOf(
            Stock(
                "DJI",
                name = "Dow Jones Industrial Average",
                current_price_cents = "738884884848484848848484848",
                currency = "CAD",
                quantity = 8000,
                current_price_timestamp = Date().time
            ), Stock(
                "^ABC",
                name = "ABC Industrial Average",
                current_price_cents = "738884884848484848848484848",
                currency = "CAD",
                quantity = 8000,
                current_price_timestamp = Date().time
            )
        )
    ) {
        ""
    }
}