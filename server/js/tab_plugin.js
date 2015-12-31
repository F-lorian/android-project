(function ($) {

    $.fn.myTab = function (options) {

        var defauts = {
            cols: ['data'],
            primary: 0,
            visible: 0
        };
        var parametres = $.extend(defauts, options);
        var element = $(this);

        var objects = {
            cols: null,
            primary: 0,
            visible: 0,
            select_list: null,
            list: null
        }

        var params = {
            intervals: 0
        }

        function initTab() {
            /*$.each(parametres, function () {
                console.log("param :"+this);
            });*/
            
            objects.cols = parametres.cols;
            
            var primary_index = objects.cols.indexOf(parametres.primary);
            var visible_index = objects.cols.indexOf(parametres.visible);
            if(primary_index != -1){
                objects.primary = primary_index;
            }
            if(visible_index != -1){
                objects.visible = visible_index;
            }
            
            objects.list = $(parametres.list).myList();
            
            console.log("primary : "+objects.primary);
            console.log("cols : "+objects.cols);
            
            var baseHTML = '<div class="list-group">'
                +'<a class="list-group-item list-group-action" onclick="$(\'#notification\').slideToggle(100); return false;">'
                    +'<span class="label label-primary inset">'
                        +'<i class="glyphicon glyphicon-plus"></i>'
                    +'</span> envoyer une notification'
                +'</a>'
                +'<div class="list-group-item list-group-title" >'
                    +'<div class="row">';
                    
            var colSize = 12/objects.cols.length;
            $.each(objects.cols, function () {
                baseHTML += '<div class="col-sm-'+colSize+'">'
                            + this
                        +'</div>';
            });
            
            baseHTML += '</div>'
                    +'</div>'
                    +'<span class="select-list">'
                    +'</span>'
                +'</div>';
            
            element.html('');
            element.append(baseHTML);
            
            //objects.select_list = element.find('.select-list');
            
        }

        function getSelectableHTML(values) {
            var html = '<a class="label label-default label-circle inset label-hover list-group-option remove" onclick="deleteUser(\'' + values[objects.primary] + '\');">' 
                + '<i class="glyphicon glyphicon-remove"></i>' 
            + '</a>' 
            + '<a class="list-group-item selectable"';
            
            //console.log("values: "+values);
            var i=0;
            $.each(values, function () {
                html += objects.cols[i]+'="'+this+'"';
                i++;
            });
            
            html += 'onclick="return false;">' 
                + '<div class="row">';
            
            var colSize = 12/values.length;
            i=0;
            $.each(values, function () {
                html += '<div class="col-sm-'+colSize+'">';
                
                if(i==0){
                    html += '<span class="label label-default inset">' 
                            + '<i class="glyphicon glyphicon-user"></i>' 
                            + '<i class="glyphicon glyphicon-ok invisible"></i>' 
                        + '</span>';
                }
                          
                html += this
                        +'</div>';
                i++;
            });
            
            html += '</div>' 
                + '</a>';
            
            return html;
        }

        function getRowHTML(text) {
            var html = '<a class="list-group-item" onclick="return false;">' 
                + '<div class="row">' 
                    + '<div class="col-sm-12">' 
                        + text 
                    + '</div>' 
                + '</div>' 
            + '</a>';

            return html;
        }

        function addSelectable(values) {

            var select_list = element.find('.select-list');
            select_list.append(getSelectableHTML(values));
            var new_item = select_list.find('.selectable').last();
            new_item.click(function (e) {
                selectable_click(this)
            });
            
            if(objects.list != null){
                var ondelete = function (e) {
                    toggleSelectable(new_item);
                };
                //console.log('setOnDelete('+email+',\'user_list\','+ondelete+');');
                objects.list.onDelete(values[objects.primary], ondelete);
            }

            return new_item;
        }
        
        function delSelectable(value) {

            var select_list = element.find('.select-list');
            var deleted = null;
            select_list.find('.selectable').each(function () {
                if ($(this).attr(objects.cols[objects.primary]) == value) {
                    $(this).prev('.list-group-option').remove();
                    $(this).prev('.list-group-options').remove();
                    this.remove();
                    deleted = $(this).attr(objects.cols[objects.visible]);
                    
                    if(objects.list != null && objects.list.valueExist(value)){
                        objects.list.delValue(value);
                    }
                }
            });
            
            return deleted;

        }

        function addRow(text) {

            var select_list = element.find('.select-list');
            var new_item = select_list.append(getRowHTML(text));

            return new_item;
        }

        function reset() {
            element.find('.select-list').html("");
        }

        function toggleSelectable(selectable) {

            var label = $(selectable).find('.label');
            var options = $(selectable).prev('.list-group-options').find('a');
            var selected = $(selectable).find('i:last-child');

            toggleClass(selectable, 'active');
            toggleClass(label, 'label-default');
            toggleClass(label, 'label-primary');
            toggleClass(options, 'label-default');
            toggleClass(options, 'label-primary');
            toggleClass(selected, 'invisible');

            console.log('toggle');
        };

        function selectable_click(selectable) {

            toggleSelectable(selectable);
            
            if(objects.list != null){
                
                var values = [];
                $.each(objects.cols, function () {
                    values.push($(selectable).attr(this));
                });
                
                if (!objects.list.valueExist(values[objects.primary])) {
                    var ondelete = function (e) {
                        toggleSelectable(selectable);
                    };
                    objects.list.addValue(values[objects.primary], values[objects.visible], ondelete);
                } else {
                    objects.list.delValue(values[objects.primary]);
                }
            }
            
        }

        initTab();

        return {
            getValues: function () {
                return objects.list.getValues();
            },
            toggleSelectable: function (selectable) {
                toggleSelectable(selectable);
            },
            addRow: function (values) {
                return addSelectable(values);
            },
            delRow: function (value) {
                return delSelectable(value);
            },
            addMessageRow: function (text) {
                return addRow(text);
            },
            reset: function () {
                element.find('.select-list').html('');
            },
            rowSelected: function (value) {
                //console.log('value exist :' + existInList(value));
                return objects.list.valueExist(value);
            },
            resetTab: function () {
                element.html('');
                element.myTab();
            }
        };
    };

})(jQuery);


$(document).ready(function () {
    
    $('.myTab').myTab();
    
   /* $('.list-group-item.selectable').click(function (e) {
        selectable_click(this)
    });*/

});