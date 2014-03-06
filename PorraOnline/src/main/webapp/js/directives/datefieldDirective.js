'use strict';

directives.directive('datefield', ['dateFilter',function(dateFilter) {
    return {
      require: 'ngModel',
      link: function (scope, element, attrs, ngModelCtrl) {
    	  
    	  ngModelCtrl.$formatters.unshift(function (modelValue) {
              return dateFilter(modelValue, 'yyyy-MM-dd');
          });

          ngModelCtrl.$parsers.unshift(function(viewValue) {
              return new Date(viewValue);
          });
      }
    };
  }]);