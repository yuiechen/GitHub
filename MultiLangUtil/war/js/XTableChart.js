(function($) {

    creatTable = function(inputData, opts) {
        console.dir(inputData);

        var ref1, ref2, result, thead, tbody, tr, th, td, diagonal, nextCell, rowKeys;

        var headers = (ref1 = inputData.head) != null ? ref1 : [];
        var bodies = (ref2 = inputData.body) != null ? ref2 : [];
        rowKeys = [];

        table = document.createElement("table");
        table.className = "XTable";
        table.id = "mytable01";
        table.style.borderCollapse = "collapse";
        // table.setAttribute("border", 1);

        thead = document.createElement("thead");
        // thead.style.display = "block";

        for (var i = 0; i < headers.length; i++) {
            var head = headers[i];
            tr = document.createElement("tr");
            var colSpanIndex = 1;
            for (var j = 0; j < head.length; j++) {

                th = document.createElement("th");
                th.className = "th";

                if (head[j] == "@") {
                    //計算colspan和rowspan ,第一個@
                    if (i == 0 && j == 0) {
                        th.setAttribute("rowspan", headers.length);
                        th.setAttribute("colspan", 1);
                        th.className = "th";
                        th.textContent = " ";
                        th.id = "diagonal";
                        diagonal = th;
                        tr.appendChild(th);
                    }
                    diagonal.colSpan = j + 1;
                    //有幾rowHead
                    if (i == 0) {
                        rowKeys.push(j);
                    }
                } else {

                    th.textContent = head[j];
                    // console.log("1"+head);
                    //往右比數值有沒有相同
                    // console.log(j + ":" + head[j]);

                    for (var k = j + 1; k <= head.length; k++) {
                        // console.log(k + ":" + head[k]);
                        // console.log("j", j);
                        // console.log("k", k);
                        if (head[j] == head[k]) {
                            colSpanIndex++;
                            // console.log("same");
                        } else {
                            th.setAttribute("colspan", colSpanIndex);
                            // console.log("colspan", colSpanIndex);
                            // console.log("k",head[k]);
                            colSpanIndex = 1;
                            j = k - 1;
                            // console.log(j, k);
                            break;
                        }
                    }



                    // head.splice(j + 1, 1);
                    // console.log("2"+head);
                    // th.setAttribute("colspan", colSpanIndex++);
                    tr.appendChild(th);
                }
                //console.log(head[j]);
                //console.log("[" + i + "," + j + "]");
            }

            thead.appendChild(tr);
        }

        table.appendChild(thead);

        // var tfoot = document.createElement("tfoot");
        // tr=document.createElement("tr");
        // tr.appendChild(document.createElement("td"));
        // tfoot.appendChild(tr);
        // table.appendChild(tfoot);

        tbody = document.createElement("tbody");
        // tbody.style.height = "200px";
        // tbody.style.overflow = "auto";
        // tbody.style.display = "block";

        for (var i = 0; i < bodies.length; i++) {
            var body = bodies[i];
            tr = document.createElement("tr");

            for (var j = 0; j < body.length; j++) {
                td = document.createElement("td");
                // console.log(rowKeys);
                if (rowKeys.indexOf(j) != -1) {
                    td.className = "col_th";
                }
                td.textContent = body[j];
                tr.appendChild(td);
            }

            // th.setAttribute("colspan", rowAttrs.length);
            // th.setAttribute("rowspan", colAttrs.length);
            tbody.appendChild(tr);
        }
        table.appendChild(tbody);

        result = table;
        return result;
    };

    //排版
    // setType = function(tableElement) {
    //     console.log(typeof(tableElement));
    //     console.log(tableElement);
    // };

    //建立表單
    $.fn.creatTable = function(inputData, opts) {
        this.empty();
        this.append(creatTable(inputData, opts));
        console.dir(this);
        console.dir(opts);
        // if(null!= opts.width && opts.width != undefined){
        //  this.css("width",opts.width);
        //  console.log("width",opts.width);
        // }
        // if(null!= opts.height && opts.height != undefined){
        //     this.css("height",opts.height);
        //     console.log("height",opts.height);
        // }

        return this;
    };

    $.fn.hasYScrollBar = function() {
        return this.get(0).scrollHeight > this.height();
    }
    $.fn.hasXScrollBar = function() {
            return this.get(0).scrollWidth > this.width();
    }
        //排版固定Header
    $.fn.fixedHeaderColumn = function(opts) {
        if (null != opts.width && opts.width != undefined) {
            this.css("width", opts.width);
            console.log("width", opts.width);
        }
        if (null != opts.height && opts.height != undefined) {
            this.css("height", opts.height);
            console.log("height", opts.height);
        }

        console.dir(this);
        // $(this).css("border", "1px solid");
        // console.log(this.find("#diagonal").outerWidth() + 1);
        // console.log(this.find("#diagonal").outerHeight() + 1);
        // console.log(this.find("table"));
        // console.log($(this).outerWidth());
        // console.log($(this).outerHeight());
        //偵測scrollBar寬度
        document.body.style.overflow = 'hidden';
        var width = document.body.clientWidth;
        document.body.style.overflow = 'scroll';
        width -= document.body.clientWidth;
        if (!width) {
            width = document.body.offsetWidth - document.body.clientWidth;
        }
        document.body.style.overflow = '';
        // alert(width);

        var scrollBarWH = width;

        var vmHeight = $(this).height();
        var vmWidth = $(this).width();

        var diagonalHeight = $(this).find("#diagonal").outerHeight(true);
        var diagonalWidth = $(this).find("#diagonal").outerWidth(true);

        var colHeaderHeight = diagonalHeight;
        var rowHeaderWidth = diagonalWidth;

        console.log(vmHeight);
        console.log(vmWidth);
        console.log(diagonalHeight);
        console.log(diagonalWidth);
        console.log(colHeaderHeight);
        console.log(rowHeaderWidth);

        // var divHeightBar = $(this).height($(this).outerHeight() + 17).outerHeight(); //17預留scrollbar空間
        // var divWidthBar = $(this).width($(this).outerWidth() + 17).outerWidth(); //17預留scrollbar

        // console.log(divHeight,divWidth,divHeighthBar,divWidthBar);

        var $table = $(this).find("table").detach();
        console.log("original Table", $table);

        var typeDiv = '<div id="typeDiv" style="position: relative;">' +
            '<div style=" width: 100%; height: 100%;display:table;">' +
            '<div style="float: left; width: 100%; display:table-row;">' +
            '<div id="divA" class="typeSetting" style="float: left; overflow: hidden; pointer-events:none;">A</div>' +
            '<div id="divB" class="typeSetting" style="overflow: hidden;pointer-events:none;">B</div>' +
            '</div>' +
            '<div style="float: left; width: 100%;height: 100%; display:table-row;">' +
            '<div id="divC" class="typeSetting" style="float: left;overflow: hidden;pointer-events:none;">C</div>' +
            '<div id="divD" class="typeSetting" style="overflow: hidden;">D</div>' +
            '</div>' +
            '</div>' +
            '</div>';

        $(this).append(typeDiv);

        console.log($("#typeDiv").outerWidth(), $("#typeDiv").outerHeight());

        var $diagonalDiv = $table.clone();
        $diagonalDiv[0].id = "diagonalDiv";
        $diagonalDiv[0].className = "diagonal";
        $("#divA").html($diagonalDiv);

        $table.find("#diagonal").className = "diagonal";
        $("#diagonalDiv").width(vmWidth);
        $("#diagonalDiv").height(vmHeight);

        $("#divA").width($("#diagonal").outerWidth(true));
        $("#divA").height($("#diagonal").outerHeight(true));

        $table.find("#diagonal").removeAttr("id");

        //$("#output").append(tab0);
        //$("#tab0").wrap("<div id='fixColScrollBar'><div id='fixCol'></div></div>");
        // $(diagonalDiv).wrap("<div>AAAAA</div>");
        // $("#rowHeaderScrollBar").height($("#divC").outerHeight() - 17 - 2);        
        // console.log(-$("#diagonal").outerHeight());

        var $colHeaderDiv = $table.clone();
        $colHeaderDiv[0].id = "colHeaderDiv";
        $colHeaderDiv[0].className = "colHeader";
        $("#divB").html("<div id='colHeaderScrollBar'></div>");
        $("#colHeaderScrollBar").append($colHeaderDiv);
        $("#colHeaderScrollBar").css("overflow-x", "scroll");
        $("#colHeaderScrollBar").css("overflow-y", "hidden");
        $("#colHeaderScrollBar").css("margin-left", -$("#diagonal").outerWidth(true));
        $("#colHeaderScrollBar").height($("#diagonal").outerHeight(true) + scrollBarWH + 20);

        $("#divB").width(vmWidth - $("#divA").outerWidth(true));
        $("#divB").height($("#diagonal").outerHeight(true));


        var $rowHeaderDiv = $table.clone();
        $rowHeaderDiv[0].id = "rowHeaderDiv";
        $rowHeaderDiv[0].className = "rowHeader";
        $("#divC").html("<div id='rowHeaderScrollBar'></div>");
        $("#rowHeaderScrollBar").append($rowHeaderDiv);
        $("#rowHeaderScrollBar").css("overflow-x", "hidden");
        $("#rowHeaderScrollBar").css("overflow-y", "scroll");
        $("#rowHeaderDiv").css("margin-top", -$("#diagonal").outerHeight(true));
        $("#rowHeaderScrollBar").width(vmWidth);
        $("#rowHeaderScrollBar").height(vmHeight - $("#divA").outerHeight(true));
        $("#divC").width($("#divA").outerWidth(true));
        $("#divC").height(vmHeight - $("#divA").outerHeight(true));


        var $bodyDiv = $table.clone();
        $bodyDiv[0].id = "bodyDiv";
        $bodyDiv[0].className = "css_body";

        $("#divD").html("<div id='bodyScrollBar'></div>");
        $("#bodyScrollBar").append($bodyDiv);
        $("#bodyScrollBar").css("overflow", "auto");
        $("#bodyDiv").css("margin-top", -$("#diagonal").outerHeight(true));
        $("#bodyDiv").css("margin-left", -$("#diagonal").outerWidth(true));
        $("#bodyScrollBar").width($("#divB").width());
        $("#bodyScrollBar").height($("#divC").height());

        /*
        console.log($("#bodyDiv").outerHeight(), $("#bodyScrollBar").outerHeight());

        // $(this).find("#divA").height(diagonalHeight);
        // $(this).find("#divA").width(diagonalWidth);
        // $(this).find("#divB").height(diagonalHeight);
        // $(this).find("#divC").width(diagonalWidth);
        // $(this).find("#divC").height(vmHeight - diagonalHeight);
        // $(this).find("#divD").height(vmHeight - diagonalHeight);


        $("#rowHeaderScrollBar").width(diagonalWidth + scrollBarWH);

        if (vmHeight > $("#diagonalDiv").height()) {
            console.log("row hasn't Scroll");

            $("#colHeaderScrollBar").width(vmWidth - diagonalWidth);
            $("#bodyScrollBar").width(vmWidth - diagonalWidth);

            if (vmWidth > $("#diagonalDiv").width()) {
                console.log("col hasn't Scroll");
                $("#bodyScrollBar").height($("#diagonalDiv").height() - diagonalHeight);
            } else {
                console.log("col has Scroll");
                $("#bodyScrollBar").height($("#diagonalDiv").height() - diagonalHeight + scrollBarWH);
            }
        } else {
            console.log("row has Scroll");

            $("#colHeaderScrollBar").width(vmWidth - diagonalWidth - scrollBarWH);

            $("#bodyScrollBar").width(vmWidth - diagonalWidth);

            if (vmWidth > $("#diagonalDiv").width()) {
                console.log("col hasn't Scroll");
                $("#bodyScrollBar").width(vmWidth - diagonalWidth);
            } else {
                console.log("col has Scroll");
                $("#diagonalDiv").width()
                $("#bodyScrollBar").width(vmWidth - diagonalWidth);
            }
        }


        //colHeader寬度
        // $("#colHeaderScrollBar").width(vmWidth - scrollBarWH - diagonalWidth);

        // $("#bodyScrollBar").width(vmWidth - rowHeaderWidth - scrollBarWH);
        // $("#colHeaderScrollBar").height(colHeaderHeight + scrollBarWH); 
        // $("#rowHeaderScrollBar").height($("#divC").outerHeight());
        // $("#bodyScrollBar").height($("#rowHeaderScrollBar").height());

        console.log($("#bodyScrollBar")[0].scrollHeight, $("#bodyScrollBar").height());
        if ($("#bodyScrollBar")[0].scrollHeight > $("#bodyScrollBar").height()) {
            console.log($("#bodyScrollBar")[0].scrollHeight, $("#bodyScrollBar").height());
        }

        if ($("#rowHeaderScrollBar").height() > $("#divC").height()) {
            // $("#rowHeaderScrollBar").height($("#divC").height());
        } else if ($("#rowHeaderScrollBar").height() < $("#divC").height()) {
            // $("#rowHeaderScrollBar").height($("#diagonalDiv").height() - diagonalHeight);
        }
        // if ($("#bodyDiv").outerHeight() >= $("#bodyScrollBar").outerHeight()) {
        //     $("#colHeaderScrollBar").width(divWidth - diagonalWidth - 6);
        // } else {
        //     $("#colHeaderScrollBar").width(divWidthBar - diagonalWidth - 6);
        // }

        // //rowHeader高度
        // console.log("h", $("#bodyScrollBar")[0].scrollWidth > $("#bodyScrollBar")[0].clientWidth);
        // console.log("h", $("#bodyScrollBar")[0].scrollWidth, $("#bodyScrollBar")[0].clientWidth);

        // if ($("#bodyDiv > tbody").outerHeight() < $("#divD").outerHeight()) {
        //     $("#bodyScrollBar").height($("#bodyDiv > tbody").outerHeight() + 20);
        // }

        // if ($("#bodyDiv > tbody").width() > divWidth) {
        //     $("#rowHeaderScrollBar").height($("#divC").outerHeight() - 17);
        // } else {
        //     $("#rowHeaderScrollBar").height($("#divC").outerHeight());
        // }

        */

        //微調scorllBar位置調整
        //判斷垂直方向row
        if ($("#bodyScrollBar").hasYScrollBar()) {
            console.log("vertical Y 垂直 HAS");
            $("#divB").width($("#divB").width() - scrollBarWH);
            $("#rowHeaderScrollBar").height($("#bodyScrollBar").height());

            if ($("#bodyScrollBar").hasXScrollBar()) {
                console.log("horizontal X 水平 HAS");
                $("#divC").height($('#bodyScrollBar').height()+ scrollBarWH);
                $("#divD").height($("#divC").height());
                $('#bodyScrollBar').height($("#divC").height());
            } else {
                console.log("horizontal X 水平 NOT");

                var colHeaderW = $("#colHeaderDiv > thead ").width() -
                    ($("#diagonal").outerWidth(true) -
                        $("#diagonal").css("border-left-width").replace(/[^-\d\.]/g, ''));
                $("#divB").width(colHeaderW);
                $("#bodyScrollBar").width(colHeaderW + scrollBarWH);
            }

        } else {
            console.log("vertical Y 垂直 NOT");
            var h = $("#rowHeaderDiv > tbody ").height();
            $("#divC").height(h + scrollBarWH);
            $("#rowHeaderScrollBar").height(h);


            if ($("#bodyScrollBar").hasXScrollBar()) {
                console.log("horizontal X 水平 HAS");
                $("#divD").height($("#divC").height());
                $('#bodyScrollBar').height($("#divC").height());
            } else {
                console.log("horizontal X 水平 NOT");
            }

        }

        //判斷水平方向col
        // if ($("#bodyScrollBar").hasXScrollBar()) {
        //     console.log("horizontal X 水平 HAS");
        //     // $("#divC").height($("#divC").height() - scrollBarWH);
        // } else {
        //     console.log("horizontal X 水平 NOT");
        // }


        $('#bodyScrollBar').scroll(function() {
            var left = $(this).scrollLeft();
            var top = $(this).scrollTop();
            console.log(left, top);
            $('#colHeaderScrollBar').scrollLeft(left);
            $('#rowHeaderScrollBar').scrollTop(top);
        });

        //停止偵測Header滾輪
        $("#colHeaderScrollBar").bind("mousewheel", function() {
            return false;
        }).attr("unselectable", "on").css('user-select', 'none').on('selectstart', false);
        $("#rowHeaderScrollBar").bind("mousewheel", function() {
            return false;
        }).attr("unselectable", "on").css('user-select', 'none').on('selectstart', false);




        console.log($colHeaderDiv);
        // $("#output").append($table);
        return this;
    };

    $.fn.createFixedNavBar = function() {
    	//alert("bar");
    	$(this).append('<nav id="bottomTools" class="navbar navbar-inverse navbar-fixed-bottom" role="navigation">'+
							'<div class="container-fluid">'+
								'<div class="navbar-header">'+
									'<div class="col-xs-1">&nbsp;</div>'+
									'<div class="col-xs-2 ">'+
										'<a href="../WechatHtml5/ListFocus.jsp">'+
											'<img src="../WechatHtml5/image/focus.jpg">'+
										'</a>'+
									'</div>'+
									'<div class="col-xs-2">&nbsp;</div>'+
									'<div class="col-xs-2 ">'+
										'<a href="../WechatHtml5/ListGuide.jsp">'+
											'<img src="../WechatHtml5/image/report.jpg">'+
										'</a>'+
									'</div>'+
//									'<div class="col-xs-2">&nbsp;</div>'+
//									'<div class="col-xs-2 ">'+
//										'<a href="../WechatHtml5/ListMessage.jsp">'+
//											'<img src="../WechatHtml5/image/message.jpg">'+
//										'</a>'+
//									'</div>'+
								'</div>'+
							'</div>'+
						'</nav>');
        return this;
    }
    
    
}(jQuery));
