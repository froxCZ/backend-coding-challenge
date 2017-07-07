"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy","$filter", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy,$filter) {
    const VAT = 0.2; //TODO: move to suitable place for constants.

    //Regexp for matching currency and amount.
    const AMOUNT_CURRENCY_REGEXP = new RegExp("^\\d+[\\.]?\\d*(\\s*[a-zA-Z]{3})?$");

	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	};

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
                $scope.clearExpense();
				loadExpenses();
            }).error(function () {
                $scope.serverErrorMessage = "Expense was not saved. Try again.";
            });
		}
	};

	$scope.clearExpense = function() {
        if ($scope.expensesform) {
            //clear form state
            $scope.expensesform.$setPristine();
            $scope.expensesform.$setUntouched();
        }
		$scope.newExpense = {};
        $scope.serverErrorMessage = null;
	};

    $scope.amountChange = function () {
        //remove all characters except for digits and dot (and comma is replaced by dot)
        if ($scope.newExpense.amount && $scope.validateAmount()) {
            var amountWithoutCurrency = $scope.newExpense.amount.replace(/[^0-9.,]/g, '').replace(/[,]/g, '.');
            $scope.newExpense.vat = $filter('number')(amountWithoutCurrency * VAT, 2);
        } else {
            $scope.newExpense.vat = 0
        }
    };

    $scope.validateAmount = function () {
        var isValid = AMOUNT_CURRENCY_REGEXP.test($scope.newExpense.amount);
        $scope.expensesform.amount.$setValidity('pattern', isValid);
        return isValid;
    };

    $scope.validateDate = function () {
        var momentDate = window.moment($scope.newExpense.date, 'DD/MM/YYYY', true);
        $scope.expensesform.date.$setValidity('dateFormat', momentDate.isValid());
    };

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
