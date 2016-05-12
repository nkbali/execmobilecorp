var app = angular.module('execMobileApp', ['angularUtils.directives.dirPagination', 'angular-md5', 'ngBusy']).config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.timeout = 600000;
}]);

app.controller('CompanyController', function($scope, $http, Company, httpFactory, User, Login, md5) {
    var customers = this;
    customers.companies = [];
    customers.user = {};
    customers.currentCompany = {};
    customers.locations = [];
    customers.currentLocation = {};
    customers.currentUser = {};
    customers.currentUser = User.getUser();

    customers.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(customers.user.username, customers.user.password);
        }
    };

    customers.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    customers.isLoggedIn = function isLoggedIn() {
        if (typeof customers.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    customers.isAdmin = function isAdmin() {
        if (typeof customers.currentUser.role === 'undefined') {
            return false;
        } else if (customers.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    customers.requestPasswordReset = function requestPasswordReset() {
        if (typeof customers.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(customers.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    if (typeof User.getUser().accessToken !== 'undefined') {
        httpFactory.getAllCountries(User).then(function(response) {
            customers.locations = response;
        });
        httpFactory.getCompanies(User).then(function(response) {
            customers.companies = response;
        });
    }
    customers.clearCurrentCompany = function() {
        customers.currentCompany = {};
    };
    customers.setCompanyForModal = function(currentCompany) {
        customers.currentCompany = currentCompany;
    };
    customers.setCurrentCompany = function(currentCompany) {
        customers.currentCompany = currentCompany;
        Company.setCompany(customers.currentCompany);
        window.location.href = "devices.html";
    };

    customers.submitForm = function submitForm(isValid) {
        if (isValid) {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/company";
            var hash = md5.createHash(customers.currentCompany.password);
            customers.currentCompany.password = hash.toString();
            if (typeof User.getUser().accessToken !== 'undefined') {
                if (typeof customers.currentCompany.companyId === "undefined") {
                    $http.post(url, customers.currentCompany, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                            customers.currentCompany = {};
                        })
                        .error(function(response, status) {
                            customers.currentCompany = {};
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                } else {
                    $http.put(url, customers.currentCompany, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                            customers.currentCompany = {};
                        })
                        .error(function(response, status) {
                            customers.currentCompany = {};
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                }
            }
        }
    };
});
app.controller('ProductController', function($scope, $http, Product, User, Login, httpFactory) {
    var plans = this;
    plans.products = [];
    plans.currentProduct = {};
    plans.currentUser = {};
    plans.currentUser = User.getUser();
    plans.user = {};
    plans.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(plans.user.username, plans.user.password);
        }
    };

    plans.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    plans.isLoggedIn = function isLoggedIn() {
        if (typeof plans.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    plans.isAdmin = function isAdmin() {
        if (typeof plans.currentUser.role === 'undefined') {
            return false;
        } else if (plans.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    plans.requestPasswordReset = function requestPasswordReset() {
        if (typeof plans.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(plans.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    plans.setCurrentProduct = function(currentProduct, linkElement) {
        plans.currentProduct = currentProduct;
        Product.setProduct(plans.currentProduct);
        if (linkElement == "details")
            window.location.href = "details.html";
        if (linkElement == "faq")
            window.location.href = "faq.html";
        if (linkElement == "support")
            window.location.href = "support.html";
        if (linkElement == "zone")
            window.location.href = "zone.html";
    };
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get("http://localhost:8080/ExecMobileRest/api/product", config)
            .success(function(data) {
                plans.products = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    plans.setProductForModal = function(currentProduct) {
        plans.currentProduct = currentProduct;
    };
    plans.clearCurrentProduct = function() {
        plans.currentProduct = {};
    };
    plans.submitForm = function(isValid) {
        if (isValid) {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/product";
            if (typeof User.getUser().accessToken !== 'undefined') {
                if (typeof plans.currentProduct.productId === "undefined") {
                    $http.post(url, plans.currentProduct, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(response, status) {
                            plans.currentProduct = {};
                            if (status == 401 || status == 403) {
                                alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                } else {
                    $http.put(url, plans.currentProduct, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(response, status) {
                            plans.currentProduct = {};
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                }
            }
        }
    };
});
app.controller('DeviceController', function($scope, $http, Company, httpFactory, User, Login) {
    var pocketwifis = this;
    pocketwifis.devices = [];
    pocketwifis.currentCompany = Company.getCompany();
    pocketwifis.currentDevice = {};
    pocketwifis.companies = [];
    pocketwifis.products = [];
    pocketwifis.currentUser = {};
    pocketwifis.currentUser = User.getUser();

    pocketwifis.user = {};
    pocketwifis.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(pocketwifis.user.username, pocketwifis.user.password);
        }
    };

    pocketwifis.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    pocketwifis.isLoggedIn = function isLoggedIn() {
        if (typeof pocketwifis.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    pocketwifis.isAdmin = function isAdmin() {
        if (typeof pocketwifis.currentUser.role === 'undefined') {
            return false;
        } else if (pocketwifis.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    pocketwifis.requestPasswordReset = function requestPasswordReset() {
        if (typeof pocketwifis.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(pocketwifis.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    var baseUrl = "http://localhost:8080/ExecMobileRest/api/company/";
    var url = baseUrl.concat(pocketwifis.currentCompany.companyId, "/device");
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get(url, config)
            .success(function(data) {
                pocketwifis.devices = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }

    if (typeof User.getUser().accessToken !== 'undefined') {
        httpFactory.getCompanies(User).then(function(response) {
            pocketwifis.companies = response;
        });
        httpFactory.getProducts(User).then(function(response) {
            pocketwifis.products = response;
        });
    }
    pocketwifis.clearCurrentDevice = function clearCurrentDevice() {
        pocketwifis.currentDevice = {};
    };
    pocketwifis.setDeviceForModal = function setDeviceForModal(device) {
        pocketwifis.currentDevice = device;
    };
    pocketwifis.modifyDevice = function modifyDevice(isValid) {
        if (isValid) {
            if (typeof User.getUser().accessToken !== 'undefined') {
                var config = {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': User.getUser().accessToken
                    }
                }
                var url = "http://localhost:8080/ExecMobileRest/api/allocated"
                $http.put(url, pocketwifis.currentDevice, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        };
    }
});
app.controller('AvailableDeviceController', function($scope, $http, httpFactory, User, Login) {
    var availablepocketwifis = this;
    availablepocketwifis.devices = [];
    availablepocketwifis.selectedDevices = [];
    availablepocketwifis.companies = [];
    availablepocketwifis.currentCompany = {};
    availablepocketwifis.currentProduct = {};
    availablepocketwifis.products = [];
    availablepocketwifis.file = {};
    availablepocketwifis.currentUser = {};
    availablepocketwifis.currentUser = User.getUser();

    availablepocketwifis.user = {};
    availablepocketwifis.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(availablepocketwifis.user.username, availablepocketwifis.user.password);
        }
    };

    availablepocketwifis.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    availablepocketwifis.isLoggedIn = function isLoggedIn() {
        if (typeof availablepocketwifis.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    availablepocketwifis.isAdmin = function isAdmin() {
        if (typeof availablepocketwifis.currentUser.role === 'undefined') {
            return false;
        } else if (availablepocketwifis.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    availablepocketwifis.requestPasswordReset = function requestPasswordReset() {
        if (typeof availablepocketwifis.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(availablepocketwifis.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    if (typeof User.getUser().accessToken !== 'undefined') {
        httpFactory.getCompanies(User).then(function(response) {
            availablepocketwifis.companies = response;
        });
        httpFactory.getProducts(User).then(function(response) {
            availablepocketwifis.products = response;
        });
    }
    availablepocketwifis.findIndexOf = function findIndexByKeyValue(deviceId) {
        for (var i = 0; i < availablepocketwifis.selectedDevices.length; i++) {
            if (availablepocketwifis.selectedDevices[i].deviceId == deviceId) {
                return i;
            }
        }
        return -1;
    };
    availablepocketwifis.toggleSelection = function toggleSelection(country) {
        var index = availablepocketwifis.findIndexOf(country.countryId);
        if (index > -1) {
            availablepocketwifis.c.splice(index, 1);
        } else {
            availablepocketwifis.selectedDevices.push(country);
        }
    };
    availablepocketwifis.period = {};
    availablepocketwifis.linkDevices = function linkDevices() {
        var url = "http://localhost:8080/ExecMobileRest/api/available";
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': User.getUser().accessToken
            }
        }
        for (var i = 0; i < availablepocketwifis.selectedDevices.length; i++) {
            availablepocketwifis.selectedDevices[i].company = availablepocketwifis.currentCompany;
            availablepocketwifis.selectedDevices[i].product = availablepocketwifis.currentProduct;
            availablepocketwifis.selectedDevices[i].period = availablepocketwifis.period.id;
        }
        if (typeof User.getUser().accessToken !== 'undefined') {
            $http.put(url, availablepocketwifis.selectedDevices, config)
                .success(function(data, status, headers, config) {
                    window.location.reload(true);
                    availablepocketwifis.selectedDevices = [];
                })
                .error(function(response, status) {
                    if (status == 401 || status == 403) {
                        if (typeof User.getUser().accessToken !== 'undefined')
                            alert("Authentication Failed");
                        Login.clearSession();
                    } else {
                        alert("There was a problem communicating with the server. Please retry again later");
                    }
                });
        }
    };

    availablepocketwifis.formData = new FormData();
    availablepocketwifis.getTheFiles = function($files) {
        angular.forEach($files, function(value, key) {
            availablepocketwifis.formData.append('file', value);
        });
    };
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get("http://localhost:8080/ExecMobileRest/api/available", config)
            .success(function(data) {
                availablepocketwifis.devices = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    availablepocketwifis.uploadNewDevices = function uploadNewDevices() {

        var uploadUrl = "http://localhost:8080/ExecMobileRest/api/available"
        if (typeof User.getUser().accessToken !== 'undefined') {
            $http.post(uploadUrl, availablepocketwifis.formData, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined,
                    'Authorization': User.getUser().accessToken
                }
            })

            .success(function() {
                    window.location.reload(true);
                })
                .error(function(response, status) {
                    if (status == 401 || status == 403) {
                        if (typeof User.getUser().accessToken !== 'undefined')
                            alert("Authentication Failed");
                        Login.clearSession();
                    } else {
                        alert(response);
                        alert("There was a problem communicating with the server. Please retry again later");
                    }
                });
        }
    };

});
app.controller('AllocatedDeviceController', function($scope, $http, httpFactory, User, Login) {
    var allocatedPocketWifis = this;
    allocatedPocketWifis.devices = [];
    allocatedPocketWifis.currentDevice = {};
    allocatedPocketWifis.companies = [];
    allocatedPocketWifis.products = [];
    allocatedPocketWifis.search = {};
    allocatedPocketWifis.currentUser = {};
    allocatedPocketWifis.currentUser = User.getUser();

    allocatedPocketWifis.user = {};
    allocatedPocketWifis.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(allocatedPocketWifis.user.username, allocatedPocketWifis.user.password);
        }
    };

    allocatedPocketWifis.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    allocatedPocketWifis.isLoggedIn = function isLoggedIn() {
        if (typeof allocatedPocketWifis.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    allocatedPocketWifis.isAdmin = function isAdmin() {
        if (typeof allocatedPocketWifis.currentUser.role === 'undefined') {
            return false;
        } else if (allocatedPocketWifis.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    allocatedPocketWifis.requestPasswordReset = function requestPasswordReset() {
        if (typeof allocatedPocketWifis.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(allocatedPocketWifis.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    if (typeof User.getUser().accessToken !== 'undefined') {
        httpFactory.getCompanies(User).then(function(response) {
            allocatedPocketWifis.companies = response;
        });
        httpFactory.getProducts(User).then(function(response) {
            allocatedPocketWifis.products = response;
        });
    }

    allocatedPocketWifis.searchDevice = function searchDevice(isValid) {
        var baseUrl = "http://localhost:8080/ExecMobileRest/api/allocated/";
        var url = baseUrl.concat(allocatedPocketWifis.search.text);
        var config = {
            headers: {
                'Authorization': User.getUser().accessToken
            }
        }
        if (typeof User.getUser().accessToken !== 'undefined') {
            $http.get(url, config)
                .success(function(data) {
                    allocatedPocketWifis.devices = data;
                })
                .error(function(response, status) {
                    if (status == 401 || status == 403) {
                        if (typeof User.getUser().accessToken !== 'undefined')
                            alert("Authentication Failed");
                        Login.clearSession();
                    } else {
                        alert("There was a problem communicating with the server. Please retry again later");
                    }
                });
        }
    };

    allocatedPocketWifis.clearCurrentDevice = function clearCurrentDevice() {
        allocatedPocketWifis.currentDevice = {};
    };
    allocatedPocketWifis.setDeviceForModal = function setDeviceForModal(device) {
        allocatedPocketWifis.currentDevice = device;
    };
    allocatedPocketWifis.modifyDevice = function modifyDevice(isValid) {
        if (isValid) {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/allocated"
            if (typeof User.getUser().accessToken !== 'undefined') {
                $http.put(url, allocatedPocketWifis.currentDevice, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        };
    };

});
app.controller('FaqController', function($scope, $http, Product, User, Login, httpFactory) {
    var questions = this;
    questions.faqs = [];
    questions.currentFaq = {};
    questions.currentProduct = Product.getProduct();
    questions.currentUser = {};
    questions.currentUser = User.getUser();

    questions.user = {};
    questions.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(questions.user.username, questions.user.password);
        }
    };

    questions.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    questions.isLoggedIn = function isLoggedIn() {
        if (typeof questions.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    questions.isAdmin = function isAdmin() {
        if (typeof questions.currentUser.role === 'undefined') {
            return false;
        } else if (questions.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    questions.requestPasswordReset = function requestPasswordReset() {
        if (typeof questions.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(questions.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    var baseUrl = "http://localhost:8080/ExecMobileRest/api/product/";
    var url = baseUrl.concat(questions.currentProduct.productId, "/faq");
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get(url, config)
            .success(function(data) {
                questions.faqs = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    questions.setFaqForModal = function(currentFaq) {
        questions.currentFaq = currentFaq;
    };
    questions.clearCurrentFaq = function() {
        questions.currentFaq = {};
    };
    questions.submitForm = function submitForm(isValid) {
        if (isValid) {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/product/faq";
            questions.currentFaq.product = {};
            questions.currentFaq.product.productId = questions.currentProduct.productId;
            if (typeof User.getUser().accessToken !== 'undefined') {
                if (typeof questions.currentFaq.faqid === "undefined") {
                    $http.post(url, questions.currentFaq, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(response, status) {
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                } else {
                    $http.put(url, questions.currentFaq, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(response, status) {
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                }
            }
        }
    };
    questions.deleteFaq = function deleteFaq() {
        if (typeof User.getUser().accessToken !== 'undefined') {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/product/faq/";
            url = url.concat(questions.currentFaq.faqid);
            if (typeof questions.currentFaq.faqid === "undefined") {
                alert("No item selected");
            } else {
                $http.delete(url, config)
                    .success(function(data, status, headers, config) {
                        questions.currentFaq = {};
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        }
    };
});
app.controller('UsageController', function($scope, $http, httpFactory, User, Login) {
    var usages = this;
    usages.usageRecords = [];
    usages.selectedYear = {};
    usages.currentYear = {};
    usages.currentUser = {};
    usages.currentUser = User.getUser();

    usages.user = {};
    usages.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(usages.user.username, usages.user.password);
        }
    };

    usages.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    usages.isLoggedIn = function isLoggedIn() {
        if (typeof usages.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    usages.isAdmin = function isAdmin() {
        if (typeof usages.currentUser.role === 'undefined') {
            return false;
        } else if (usages.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    usages.requestPasswordReset = function requestPasswordReset() {
        if (typeof usages.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(usages.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    usages.currentYear.id = new Date().getFullYear();
    usages.years = [];
    for (var i = 0; i < 21; i++) {
        var newYear = {};
        newYear.id = usages.currentYear.id - i;
        usages.years.push(newYear);
    }
    usages.selectedMonth = {};
    usages.currentMonth = {};
    usages.months = [];
    for (var i = 1; i < 13; i++) {
        var newMonth = {};
        newMonth.id = i;
        if (i == 1) {
            newMonth.name = 'January';
        }
        if (i == 2) {
            newMonth.name = 'February';
        }
        if (i == 3) {
            newMonth.name = 'March';
        }
        if (i == 4) {
            newMonth.name = 'April';
        }
        if (i == 5) {
            newMonth.name = 'May';
        }
        if (i == 6) {
            newMonth.name = 'June';
        }
        if (i == 7) {
            newMonth.name = 'July';
        }
        if (i == 8) {
            newMonth.name = 'August';
        }
        if (i == 9) {
            newMonth.name = 'September';
        }
        if (i == 10) {
            newMonth.name = 'October';
        }
        if (i == 11) {
            newMonth.name = 'November';
        }
        if (i == 12) {
            newMonth.name = 'December';
        }
        usages.months.push(newMonth);
    }

    usages.getMonthlyUsage = function getMonthlyUsage(isValid) {
        if (isValid) {
            var baseUrl = "http://localhost:8080/ExecMobileRest/api/usageHistory/";
            var url = baseUrl.concat(usages.selectedMonth.id, "/", usages.selectedYear.id);
            var config = {
                headers: {
                    'Authorization': User.getUser().accessToken
                }
            }
            if (typeof User.getUser().accessToken !== 'undefined') {
                $http.get(url, config)
                    .success(function(data) {
                        usages.usageRecords = data;

                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        }
    };
    usages.formData = new FormData();
    usages.getTheFiles = function($files) {
        angular.forEach($files, function(value, key) {
            usages.formData.append('file', value);
        });
    };
    usages.uploadUsage = function uploadNewDevices() {

        var uploadUrl = "http://localhost:8080/ExecMobileRest/api/usageHistory/monthlyUsage"
        if (typeof User.getUser().accessToken !== 'undefined') {
            $http.post(uploadUrl, usages.formData, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined,
                    'Authorization': User.getUser().accessToken
                }
            })

            .success(function() {
                window.location.reload(true);
            })

            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
        }
    };

});
app.controller('ReportController', function($scope, $http, httpFactory, User, Company, Login) {
    var reports = this;
    reports.currentCompany = {};
    reports.currentDevice = {};
    reports.companies = [];
    reports.devices = [];
    reports.yearlyUsageReport = {};
    reports.locationBasedUsageReport = {};
    reports.selectedYear = {};
    reports.currentYear = {};
    reports.currentYear.id = new Date().getFullYear();
    reports.years = [];
    for (var i = 0; i < 21; i++) {
        var newYear = {};
        newYear.id = reports.currentYear.id + i;
        reports.years.push(newYear);
    }
    reports.currentUser = {};
    reports.currentUser = User.getUser();

    reports.user = {};
    reports.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(reports.user.username, reports.user.password);
        }
    };

    reports.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    reports.isLoggedIn = function isLoggedIn() {
        if (typeof reports.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    reports.isAdmin = function isAdmin() {
        if (typeof reports.currentUser.role === 'undefined') {
            return false;
        } else if (reports.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    reports.requestPasswordReset = function requestPasswordReset() {
        if (typeof reports.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(reports.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    reports.resetForm = function resetForm() {
        reports.currentCompany = {};
        reports.currentDevice = {};
        reports.selectedYear = {};
    };
    reports.loadDevices = function loadDevices() {
        reports.currentDevice = {};
        var baseUrl = "http://localhost:8080/ExecMobileRest/api/company/";
        var url = baseUrl.concat(reports.currentCompany.companyId, "/device");
        if (typeof User.getUser().accessToken !== 'undefined') {
            httpFactory.getDevices(url, User).then(function(response) {
                reports.devices = response;
            });
        }
    };
    if (reports.isAdmin()) {
        if (typeof User.getUser().accessToken !== 'undefined') {
            httpFactory.getCompanies(User).then(function(response) {
                reports.companies = response;
            });
        }
    } else {
        reports.currentCompany = Company.getCompany();
        reports.loadDevices();
    }

    reports.getReport = function getReport(isValid) {
        var yearlyReportBaseUrl = "http://localhost:8080/ExecMobileRest/api/usageHistory/report/";
        var locationReportBaseUrl = "http://localhost:8080/ExecMobileRest/api/usageHistory/report/location/";

        if (isValid) {
            if (typeof User.getUser().accessToken !== 'undefined') {
                if (typeof reports.currentCompany.companyId === "undefined" && typeof reports.currentDevice.deviceId === "undefined") {

                    var usageUrl = yearlyReportBaseUrl.concat(reports.selectedYear.id);
                    var locationUrl = locationReportBaseUrl.concat(reports.selectedYear.id);
                    httpFactory.getYearlyUsageReport(usageUrl, User).then(function(response) {
                        reports.yearlyUsageReport = response;
                    });
                    httpFactory.getLocationBasedUsageReport(locationUrl, User).then(function(response) {
                        reports.locationBasedUsageReport = response;
                    });
                } else if (typeof reports.currentDevice.deviceId === "undefined") {
                    var usageUrlForCompany = yearlyReportBaseUrl.concat(reports.currentCompany.companyId, "/", reports.selectedYear.id);
                    var locationUrlForCompany = locationReportBaseUrl.concat(reports.currentCompany.companyId, "/", reports.selectedYear.id);
                    httpFactory.getYearlyUsageReport(usageUrlForCompany, User).then(function(response) {
                        reports.yearlyUsageReport = response;
                    });
                    httpFactory.getLocationBasedUsageReport(locationUrlForCompany, User).then(function(response) {
                        reports.locationBasedUsageReport = response;
                    });
                } else {
                    var usageUrlForDevice = yearlyReportBaseUrl.concat("device/", reports.currentDevice.deviceId, "/", reports.selectedYear.id);
                    var locationUrlForDevice = locationReportBaseUrl.concat("device/", reports.currentDevice.deviceId, "/", reports.selectedYear.id);
                    httpFactory.getYearlyUsageReport(usageUrlForDevice, User).then(function(response) {
                        reports.yearlyUsageReport = response;
                    });
                    httpFactory.getLocationBasedUsageReport(locationUrlForDevice, User).then(function(response) {
                        reports.locationBasedUsageReport = response;
                    });
                }
            }
        }
    };

});
app.controller('SupportController', function($scope, $http, Product, User, httpFactory, Login) {
    var support = this;
    support.supportInfo = {};
    support.currentProduct = Product.getProduct();
    support.currentUser = {};
    support.currentUser = User.getUser();

    support.user = {};
    support.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(support.user.username, support.user.password);
        }
    };

    support.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    support.isLoggedIn = function isLoggedIn() {
        if (typeof support.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    support.isAdmin = function isAdmin() {
        if (typeof support.currentUser.role === 'undefined') {
            return false;
        } else if (support.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    support.requestPasswordReset = function requestPasswordReset() {
        if (typeof support.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(support.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    var baseUrl = "http://localhost:8080/ExecMobileRest/api/product/";
    var url = baseUrl.concat(support.currentProduct.productId, "/support");
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get(url, config)
            .success(function(data) {
                support.supportInfo = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    support.addSupportInfo = function addSupportInfo() {
        var updateUrl = "http://localhost:8080/ExecMobileRest/api/product/support";
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': User.getUser().accessToken
            }
        }
        support.supportInfo.product = {};
        support.supportInfo.product.productId = support.currentProduct.productId;
        var myJSONString = angular.toJson(support.supportInfo);
        var myEscapedJSONString = myJSONString.replace(/\\n/g, "\\n")
            .replace(/\\'/g, "\\'")
            .replace(/\\"/g, '\\"')
            .replace(/\\&/g, "\\&")
            .replace(/\\r/g, "\\r")
            .replace(/\\t/g, "\\t")
            .replace(/\\b/g, "\\b")
            .replace(/\\f/g, "\\f");
        if (typeof User.getUser().accessToken !== 'undefined') {
            if (typeof support.supportInfo.productSupportId === "undefined") {
                $http.post(updateUrl, myEscapedJSONString, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            } else {
                $http.put(updateUrl, myEscapedJSONString, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        }
    };
});
app.controller('PricePlanController', function($scope, $http, $q, Product, httpFactory, User, Login) {
    var priceplan = this;
    priceplan.productDetails = {};
    priceplan.allCountries = [];
    priceplan.currentUser = {};
    priceplan.currentUser = User.getUser();

    priceplan.user = {};
    priceplan.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(priceplan.user.username, priceplan.user.password);
        }
    };

    priceplan.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    priceplan.isLoggedIn = function isLoggedIn() {
        if (typeof priceplan.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    priceplan.isAdmin = function isAdmin() {
        if (typeof priceplan.currentUser.role === 'undefined') {
            return false;
        } else if (priceplan.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    priceplan.requestPasswordReset = function requestPasswordReset() {
        if (typeof priceplan.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(priceplan.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    priceplan.currentProduct = Product.getProduct();
    var baseUrl = "http://localhost:8080/ExecMobileRest/api/product/";
    var url = baseUrl.concat(priceplan.currentProduct.productId, "/details");
    if (typeof User.getUser().accessToken !== 'undefined') {
        httpFactory.getAllCountries(User).then(function(response) {
            priceplan.allCountries = response;
        });
        httpFactory.getProductDetails(url, User).then(function(response) {
            priceplan.productDetails = response;
        });
    }
    priceplan.findIndexOf = function findIndexByKeyValue(countryId) {
        for (var i = 0; i < priceplan.productDetails.productCoverage.length; i++) {
            if (priceplan.productDetails.productCoverage[i].countryId == countryId) {
                return i;
            }
        }
        return -1;
    };
    priceplan.toggleSelection = function toggleSelection(country) {
        var index = priceplan.findIndexOf(country.countryId);
        if (index > -1) {
            priceplan.productDetails.productCoverage.splice(index, 1);
        } else {
            priceplan.productDetails.productCoverage.push(country);
        }
    };
    priceplan.addPricePlan = function addPricePlan() {
        var updateUrl = "http://localhost:8080/ExecMobileRest/api/product/details";
        var config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': User.getUser().accessToken
            }
        }
        priceplan.productDetails.productPricePlan.product = {};
        priceplan.productDetails.productPricePlan.product.productId = priceplan.currentProduct.productId;
        var myJSONString = angular.toJson(priceplan.productDetails);
        var myEscapedJSONString = myJSONString.replace(/\\n/g, "\\n")
            .replace(/\\'/g, "\\'")
            .replace(/\\"/g, '\\"')
            .replace(/\\&/g, "\\&")
            .replace(/\\r/g, "\\r")
            .replace(/\\t/g, "\\t")
            .replace(/\\b/g, "\\b")
            .replace(/\\f/g, "\\f");
        if (typeof User.getUser().accessToken !== 'undefined') {
            if (typeof priceplan.productDetails.productPricePlan.productPricePlanId === "undefined") {
                $http.post(updateUrl, myEscapedJSONString, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            } else {
                $http.put(updateUrl, myEscapedJSONString, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        }
    };
});
app.controller('ZoneController', function($scope, $http, Product, User, httpFactory, Login) {
    var areas = this;
    areas.zones = [];
    areas.currentZone = {};
    areas.currentProduct = Product.getProduct();
    areas.currentUser = {};
    areas.currentUser = User.getUser();

    areas.user = {};
    areas.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(areas.user.username, areas.user.password);
        }
    };

    areas.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    areas.isLoggedIn = function isLoggedIn() {
        if (typeof areas.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    areas.isAdmin = function isAdmin() {
        if (typeof areas.currentUser.role === 'undefined') {
            return false;
        } else if (areas.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    areas.requestPasswordReset = function requestPasswordReset() {
        if (typeof areas.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(areas.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };
    var baseUrl = "http://localhost:8080/ExecMobileRest/api/product/";
    var url = baseUrl.concat(areas.currentProduct.productId, "/zone");
    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get(url, config)
            .success(function(data) {
                areas.zones = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    areas.setZoneForModal = function(currentZone) {
        areas.currentZone = currentZone;
    };
    areas.clearCurrentZone = function() {
        areas.currentZone = {};
    };
    areas.submitForm = function submitForm(isValid) {
        if (isValid) {
            var updateUrl = "http://localhost:8080/ExecMobileRest/api/product/zone";
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            areas.currentZone.product = {};
            areas.currentZone.product.productId = areas.currentProduct.productId;
            if (typeof User.getUser().accessToken !== 'undefined') {
                if (typeof areas.currentZone.zoneid === "undefined") {
                    $http.post(updateUrl, areas.currentZone, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(response, status) {
                            if (status == 401 || status == 403) {
                                if (typeof User.getUser().accessToken !== 'undefined')
                                    alert("Authentication Failed");
                                Login.clearSession();
                            } else {
                                alert("There was a problem communicating with the server. Please retry again later");
                            }
                        });
                } else {
                    $http.put(updateUrl, areas.currentZone, config)
                        .success(function(data, status, headers, config) {
                            window.location.reload(true);
                        })
                        .error(function(data, status, header, config) {
                            alert(data);
                        });
                }
            }
        }
    };
});
app.controller('AppUsersController', function($scope, $http, Company, httpFactory, User, Login, md5) {
    var appUsers = this;
    appUsers.users = [];
    appUsers.user = {};
    appUsers.mailContent = {};
    appUsers.mailContent.receivers = [];
    appUsers.currentUser = {};
    appUsers.currentUser = User.getUser();



    appUsers.tryLogin = function tryLogin(isValid) {
        if (isValid) {
            Login.tryLogin(appUsers.user.username, appUsers.user.password);
        }
    };

    appUsers.tryLogout = function tryLogout() {
        Login.tryLogout();
    }

    appUsers.isLoggedIn = function isLoggedIn() {
        if (typeof appUsers.currentUser.username === 'undefined') {
            return false;
        } else {
            return true;
        }
    };
    appUsers.isAdmin = function isAdmin() {
        if (typeof appUsers.currentUser.role === 'undefined') {
            return false;
        } else if (appUsers.currentUser.role === 'Admin') {
            return true;
        } else {
            return false;
        }

    };
    appUsers.requestPasswordReset = function requestPasswordReset() {
        if (typeof appUsers.user.username === 'undefined') {
            alert("Please enter username");
        } else {
            httpFactory.requestPasswordReset(appUsers.user.username).then(function(response) {
                alert("System Administrators have been notified of your request and will get back to you soon.")
            });
        }
    };

    var config = {
        headers: {
            'Authorization': User.getUser().accessToken
        }
    }
    var url = "http://localhost:8080/ExecMobileRest/api/appUsers";
    if (typeof User.getUser().accessToken !== 'undefined') {
        $http.get(url, config)
            .success(function(data) {
                appUsers.users = data;
            })
            .error(function(response, status) {
                if (status == 401 || status == 403) {
                    if (typeof User.getUser().accessToken !== 'undefined')
                        alert("Authentication Failed");
                    Login.clearSession();
                } else {
                    alert("There was a problem communicating with the server. Please retry again later");
                }
            });
    }
    appUsers.clearMailContent = function clearMailContent() {
        appUsers.mailContent = {};
    };
    appUsers.findIndexOf = function findIndexOf(email) {
        for (var i = 0; i < appUsers.mailContent.receivers.length; i++) {
            if (appUsers.mailContent.receivers[i] == email) {
                return i;
            }
        }
        return -1;
    };
    appUsers.toggleSelection = function toggleSelection(email) {
        var index = appUsers.findIndexOf(email);
        if (index > -1) {
            appUsers.c.splice(index, 1);
        } else {
            appUsers.mailContent.receivers.push(email);
        }
    };


    appUsers.sendMail = function sendMail(isValid) {
        if (isValid) {
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': User.getUser().accessToken
                }
            }
            var url = "http://localhost:8080/ExecMobileRest/api/appUsers";
            if (typeof User.getUser().accessToken !== 'undefined') {
                $http.post(url, appUsers.mailContent, config)
                    .success(function(data, status, headers, config) {
                        window.location.reload(true);
                        appUsers.mailContent = {};
                    })
                    .error(function(response, status) {
                        appUsers.mailContent = {};
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            }
        }
    };
});
app.factory('Company', function($window) {

    var KEY = 'App.Company';

    return {
        getCompany: function() {
            var company = $window.sessionStorage.getItem(KEY);
            if (company) {
                company = JSON.parse(company);
            }
            return company || {}
        },
        setCompany: function(currentCompany) {
            var company = $window.sessionStorage.getItem(KEY);
            if (company)
                $window.sessionStorage.removeItem(KEY);
            $window.sessionStorage.setItem(KEY, JSON.stringify(currentCompany));
        }
    };
});
app.factory('Product', function($window) {

    var KEY = 'App.Product';

    return {
        getProduct: function() {
            var product = $window.sessionStorage.getItem(KEY);
            if (product) {
                product = JSON.parse(product);
            }
            return product || {};
        },
        setProduct: function(currentProduct) {
            var product = $window.sessionStorage.getItem(KEY);
            if (product)
                $window.sessionStorage.removeItem(KEY);
            $window.sessionStorage.setItem(KEY, JSON.stringify(currentProduct));
        }
    };
});
app.factory('User', function($window) {

    var KEY = 'App.User';

    return {
        getUser: function() {
            var user = $window.sessionStorage.getItem(KEY);
            if (user) {
                user = JSON.parse(user);
            }
            return user || {};
        },
        setUser: function(currentUser) {
            var user = $window.sessionStorage.getItem(KEY);
            if (user)
                $window.sessionStorage.removeItem(KEY);
            $window.sessionStorage.setItem(KEY, JSON.stringify(currentUser));
        }
    };
});
app.factory('Login', function($window, $http, md5, Company, User) {
    return {
        tryLogin: function(username, password) {
            var url = "http://localhost:8080/ExecMobileRest/api/login";
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            var hash = md5.createHash(password);
            var key = CryptoJS.enc.Hex.parse(hash);
            var iv = CryptoJS.enc.Hex.parse('0000000000000000');
            var paddingChar = ' ';
            var size = 16;
            var x = username.length % size;
            var padLength = size - x;
            var paddedUsername = username;
            for (var i = 0; i < padLength; i++) {
                paddedUsername += paddingChar;
            }
            var encryptedToken = CryptoJS.AES.encrypt(paddedUsername, key, {
                iv: iv,
                padding: CryptoJS.pad.Pkcs7,
                mode: CryptoJS.mode.CBC
            });
            var config = {
                headers: {
                    'Content-Type': 'application/json',
                    'RequestID': uuid
                }
            }
            var loginCredentials = {};
            loginCredentials.username = username;
            loginCredentials.loginToken = encryptedToken.toString();
            $http.post(url, loginCredentials, config)
                .success(function(data, status, headers, config) {
                    User.setUser(data.user);
                    Company.setCompany(data.company);
                    if (data.user['role'] === 'Admin')
                        window.location.reload(true);
                    else
                        window.location.href = "devices.html";
                })
                .error(function(response, status) {
                    if (status == 401) {
                        alert("Authentication Failed");
                    } else if (status == 403) {
                        alert("Session limit reached");
                    } else {
                        alert("There was a problem communicating with the server. Please retry again later");
                    }
                });
        },
        tryLogout: function() {
            var url = "http://localhost:8080/ExecMobileRest/api/logout";
            var config = {
                headers: {
                    'Authorization': User.getUser().accessToken
                }
            };
            $http.get(url, config)
                .success(function(data) {
                    User.setUser(null);
                    Company.setCompany(null);
                    window.location.reload(true);
                });
        },
        clearSession: function() {
            User.setUser(null);
            Company.setCompany(null);
            window.location.reload(true);
        }
    };
});
app.factory('httpFactory', ['$http', '$q',
    function($http, $q) {
        return {
            getProductDetails: function(url, User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get(url, config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getAllCountries: function(User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get("http://localhost:8080/ExecMobileRest/api/country", config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getCompanies: function(User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get("http://localhost:8080/ExecMobileRest/api/company", config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getProducts: function(User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get("http://localhost:8080/ExecMobileRest/api/product", config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getDevices: function(url, User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get(url, config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getYearlyUsageReport: function(url, User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get(url, config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            getLocationBasedUsageReport: function(url, User) {
                var deferred = $q.defer();
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }

                $http.get(url, config)
                    .success(function(response) {
                        deferred.resolve(response);
                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
                return deferred.promise;

            },
            requestPasswordReset: function(username) {
                var deferred = $q.defer();
                var baseUrl = "http://localhost:8080/ExecMobileRest/api/login/requestPasswordReset/";
                var url = baseUrl.concat(username);
                $http.get(url)
                    .success(function(response) {
                        deferred.resolve(response);
                    });
                return deferred.promise;
            }
        }
    }
]);
app.directive('exportToCsvLocation', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var el = element[0];
            element.bind('click', function(e) {
                var csvString = "";
                csvString = csvString + "Device,Product,Company,Active,";
                for (var i = 0; i < scope.reports.locationBasedUsageReport.countries.length; i++) {
                    csvString = csvString + scope.reports.locationBasedUsageReport.countries[i] + ",";
                }
                csvString = csvString + "Total\n";
                for (var i = 0; i < scope.reports.locationBasedUsageReport.usageRecords.length; i++) {
                    var usageRecord = scope.reports.locationBasedUsageReport.usageRecords[i];
                    csvString = csvString + usageRecord["deviceId"] + "," + usageRecord["product"] + "," + usageRecord["company"] + "," + usageRecord["isActive"] + ",";
                    var usage = usageRecord["usage"];
                    for (var j = 0; j < scope.reports.locationBasedUsageReport.countries.length; j++) {
                        var country = scope.reports.locationBasedUsageReport.countries[j];
                        csvString = csvString + usage[country] + ",";
                    }
                    csvString = csvString + usageRecord["total"] + "\n";
                }
                var a = $('<a/>', {
                    style: 'display:none',
                    href: 'data:application/octet-stream;base64,' + btoa(csvString),
                    download: 'LocationBasedUsageReport.csv'
                }).appendTo('body')
                a[0].click()
                a.remove();
            });
        }
    }
});
app.directive('exportToCsvYearly', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var el = element[0];
            element.bind('click', function(e) {
                var csvString = "";
                csvString = csvString + "Device,Product,Company,Active,201601,201602,201603,201604,201605,201606,201607,201608,201609,201610,201611,201612,Total\n";
                for (var i = 0; i < scope.reports.yearlyUsageReport.usageRecords.length; i++) {
                    var usageRecord = scope.reports.yearlyUsageReport.usageRecords[i];
                    csvString = csvString + usageRecord["deviceID"] + "," + usageRecord["product"] + "," + usageRecord["company"] + "," + usageRecord["isActive"] + ",";
                    csvString = csvString + usageRecord["usage1"] + ",";
                    csvString = csvString + usageRecord["usage2"] + ",";
                    csvString = csvString + usageRecord["usage3"] + ",";
                    csvString = csvString + usageRecord["usage4"] + ",";
                    csvString = csvString + usageRecord["usage5"] + ",";
                    csvString = csvString + usageRecord["usage6"] + ",";
                    csvString = csvString + usageRecord["usage7"] + ",";
                    csvString = csvString + usageRecord["usage8"] + ",";
                    csvString = csvString + usageRecord["usage9"] + ",";
                    csvString = csvString + usageRecord["usage10"] + ",";
                    csvString = csvString + usageRecord["usage11"] + ",";
                    csvString = csvString + usageRecord["usage12"] + ",";
                    csvString = csvString + usageRecord["total"] + "\n";
                }
                var a = $('<a/>', {
                    style: 'display:none',
                    href: 'data:application/octet-stream;base64,' + btoa(csvString),
                    download: 'YearlyUsageReport.csv'
                }).appendTo('body')
                a[0].click()
                a.remove();
            });
        }
    }
});
app.directive('exportToCsvUsers', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var el = element[0];
            element.bind('click', function(e) {
                var csvString = "";
                csvString = csvString + "Email,Last Access\n";
                for (var i = 0; i < scope.appUsers.users.length; i++) {
                    var user = scope.appUsers.users[i];
                    csvString = csvString + user["email"] + "," + new Date(user["lastAccessTime"].toLocaleString()); + "\n";

                }
                var a = $('<a/>', {
                    style: 'display:none',
                    href: 'data:application/octet-stream;base64,' + btoa(csvString),
                    download: 'YearlyUsageReport.csv'
                }).appendTo('body')
                a[0].click()
                a.remove();
            });
        }
    }
});
app.directive('ngFiles', ['$parse', function($parse) {

    function fn_link(scope, element, attrs) {
        var onChange = $parse(attrs.ngFiles);
        element.on('change', function(event) {
            onChange(scope, {
                $files: event.target.files
            });
        });
    };

    return {
        link: fn_link
    }
}]);
app.directive('exportToCsvAllocatedDevices', function($http, User, Login) {
    return {
        link: function(scope, element, attrs) {
            var el = element[0];
            element.bind('click', function(e) {

                scope.devices = [];
                var url = "http://localhost:8080/ExecMobileRest/api/allocated";
                var config = {
                    headers: {
                        'Authorization': User.getUser().accessToken
                    }
                }
                $http.get(url, config)
                    .success(function(data) {
                        scope.devices = data;

                        var csvString = "";
                        csvString = csvString + "DeviceID,IMEI,Company,Product,InvoiceName,ZTERef,Period,Invoice No,RINV No,MSISDN,Sim,Comment,Support\n";
                        for (var i = 0; i < scope.devices.length; i++) {
                            var device = scope.devices[i];
                            var company = device["company"];
                            var product = device["product"];
                            csvString = csvString + device["deviceId"] + "," + device["imei"] + "," + company["name"] + "," + product["name"] + "," + device["invoiceName"] + "," + device["zteref"] + "," + device["period"] + "," + device["invoiceNumber"] + "," + device["rinvNo"] + "," + device["msisdn"] + "," + device["sim"] + "," + device["comment"] + "," + device["support"] + "\n";
                        }
                        var a = $('<a/>', {
                            style: 'display:none',
                            href: 'data:application/octet-stream;base64,' + btoa(csvString),
                            download: 'DeviceList.csv'
                        }).appendTo('body');
                        a[0].click();
                        a.remove();

                    })
                    .error(function(response, status) {
                        if (status == 401 || status == 403) {
                            if (typeof User.getUser().accessToken !== 'undefined')
                                alert("Authentication Failed");
                            User.setUser(null);
                            Login.clearSession();
                        } else {
                            alert("There was a problem communicating with the server. Please retry again later");
                        }
                    });
            });
        }
    }
});
app.directive("passwordVerify", function() {
    return {
        require: "ngModel",
        scope: {
            passwordVerify: '='
        },
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(function() {
                var combined;

                if (scope.$$passwordVerify || ctrl.$viewValue) {
                    combined = scope.$$passwordVerify + '_' + ctrl.$viewValue;
                }
                return combined;
            }, function(value) {
                if (value) {
                    ctrl.$parsers.unshift(function(viewValue) {
                        var origin = scope.passwordVerify;
                        if (origin !== viewValue) {
                            ctrl.$setValidity("passwordVerify", false);
                            return undefined;
                        } else {
                            ctrl.$setValidity("passwordVerify", true);
                            return viewValue;
                        }
                    });
                }
            });
        }
    };
});