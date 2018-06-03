$(function () {
    $(document).ready(function() {
        var url = window.location.href;
        var params = url.slice(url.indexOf('?') + 1);

        var divs$ = [];

        function updateAllDivsInTheList() {
            $(".time").each(function (i, el) {
                var date = moment(new Date($(el).attr('data-date'))).utc();
                $(el).html(date.fromNow());
            });
            setInterval(updateAllDivsInTheList, 60000);
        }

        $.getJSON( "http://localhost/api/v1/proxy?" + params, function( data ) {

            $("#last-update").append('' +
                'Last updated: <span class="time" data-date="'+ data[0].lastUpdate +'"></span>'
            );

            $.each(data, function( key, val ) {
                $("#proxy-list").append('' +
                    '<div class="media text-muted pt-3"> ' +
                    '<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray"> ' +
                    '<strong class="d-block text-gray-dark">Country: </strong>'+ val.country+' </p> ' +
                    '<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray"> ' +
                    '<strong class="d-block text-gray-dark">Host: </strong>'+ val.host+' </p> ' +
                    '<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray"> ' +
                    '<strong class="d-block text-gray-dark">Port: </strong>'+ val.port+' </p> ' +
                    '<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">\n' +
                    '<strong class="d-block text-gray-dark">Status: </strong>\n' +
                    (val.status === 'active' ?
                        '<span class=\'dot dot-sm dot-success\'></span>Active'
                        :'<span class=\'dot dot-sm dot-danger\'></span>Inactive')
                    +
                    '</p>\n' +
                    '<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">\n' +
                    '<strong class="d-block text-gray-dark">Last Update: </strong>\n' +
                    '<span class="time" data-date="'+ val.lastUpdate +'"></span>\n' +
                    '</p> </div>');

            });

            updateAllDivsInTheList();

        });


    });
});
