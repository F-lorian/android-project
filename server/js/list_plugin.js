(function ($) {

    $.fn.myList = function (options) {

        var defauts = {
            "vitesseFadeOut": "slow",
            "vitesseFadeIn": "fast"
        };
        var parametres = $.extend(defauts, options);
        var element = $(this);

        var objects = {
            objs: null,
            lists: null,
            input: null
        }

        var params = {
            intervals: 0
        }


        function initList() {
            //element.data('value', '');
            objects.objs = [];
            objects.list = [];
            objects.input = null;
        }


        function getList() {
            return objects.lists[0];
        }

        function remove_from_list(value) {

            if (existInList(value)) {
                objects.list.splice(objects.list.indexOf(value), 1);
                console.log('suppression de ' + value);

                $.each(element.find('span'), function () {
                    if ($(this).attr('value') == value) {
                        $(this).remove();

                    }
                });
            }

            console.log('element supprimé : ');
            console.log(objects.list);
        }

        function existInList(value) {
            return objects.list.indexOf(value) != -1;
        }

        function getItemHTML(value, showed_value) {
            var html = "<span class='label label-primary inset' value='" + value + "' id='" + showed_value + "container'>" 
                + showed_value 
                + "<a id='" + showed_value +"remove' onclick='return false;' class='glyphicon glyphicon-remove remove'>"
                + "</a>"
            + "</span>";

            return html;
        }
        
        function add_to_list_input(showed_value, ondelete) {

            var value = objects.input.val();

            if (value != "null") {


                //var filtered_value = filterMail(value);
                //-----------------------------------------
                var notempty = value.length && value != " ";
                var exist = existInList(value, objects.list);


                if (!exist && notempty) {
                    var objHTML = getItemHTML(value, showed_value);

                    element.append(objHTML);

                    var obj = element.find('span').last();
                    var delete_button = $(obj).find('a');
                    $(obj).hide()
                    $(obj).fadeIn(100);

                    delete_button.click(function (e) {
                        remove_from_list(value);
                    });

                    if (ondelete != null) {
                        delete_button.click(ondelete);
                    }

                    objects.list.push(value);

                    console.log(objects.list);
                }

            }
        }


        function add_to_list(value, showed_value, ondelete) {

            if (value != "null") {

                //var showed_value = filterMail(value);
                //-----------------------------------------
                var notempty = value.length && value != " ";
                var exist = existInList(value, objects.list);


                if (!exist && notempty) {

                    var objHTML = getItemHTML(value, showed_value);

                    element.append(objHTML);

                    var obj = element.find('span').last();
                    var delete_button = $(obj).find('a');
                    $(obj).hide()
                    $(obj).fadeIn(100);

                    delete_button.click(function (e) {
                        remove_from_list(value);
                    });

                    if (ondelete != null) {
                        delete_button.click(ondelete);
                    }

                    objects.list.push(value);

                    console.log(objects.list);
                }
            }
        }

        function setOnDelete(value, ondelete) {
            console.log('SET ON DELETE');
            if (value != "null") {
                console.log('value != null');

                var filtered_value = filterMail(value);
                //-----------------------------------------
                var notempty = value.length && value != " ";
                var exist = existInList(value);
                console.log(notempty);
                console.log(exist);
                console.log(ondelete);
                if (exist && notempty) {

                    $.each(element.find('span'), function () {
                        if ($(this).attr('value') == value) {
                            var delete_button = $(this).find('a');
                            console.log('OBJECT FOUND');
                            delete_button.click(ondelete);
                        }
                    });
                }
            }
        }


        function filterMail(value) {

            if (value != "null") {
                var filtered_value;

                var reg = new RegExp("[ ]+", "g");
                var reg3 = new RegExp("#+", "g");
                var reg4 = new RegExp("@+", "g");
                var reg5 = new RegExp("[.]+", "g");

                value = value.replace(reg, ' ');
                filtered_value = value;
                filtered_value = filtered_value.replace(reg4, '_');
                filtered_value = filtered_value.replace(reg5, '_');
                console.log(value);
                console.log(filtered_value);

                return filtered_value;
            }
        }

        initList();

        return {

            valueExist: function (value) {
                console.log('value exist :' + existInList(value));
                return existInList(value);
            },
            addValue: function (value, showed_value, ondelete) {
                add_to_list(value, showed_value, ondelete);
            },
            onDelete: function (value, ondelete) {
                setOnDelete(value, ondelete);
            },
            delValue: function (value) {
                remove_from_list(value);
            },
            getValues: function () {
               /* var res = [];

                $(this).children("span").each(function () {

                    res.push($(this).data("value"));
                });

                return res;*/
                
                return objects.list;
            },
            resetList: function () {
                element.html('');
                element.myList();
            }
        };
    };

})(jQuery);


$(document).ready(function () {

    $('.myList').myList();

});