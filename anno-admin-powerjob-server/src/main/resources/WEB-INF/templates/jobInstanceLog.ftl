<!DOCTYPE html>
<html>
<head>
    <title>日志信息</title>
    <style>
        /* 样式表可以根据需要进行自定义 */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        h1 {
            text-align: center;
        }

        #log-container {
            overflow-y: auto; /* 当内容超过最大高度时出现垂直滚动条 */
            overflow-x: auto; /* 当内容超过水平空间时出现水平滚动条 */
        }

        .log-entry {
            padding: 10px;
            white-space: pre-line;
            overflow-x: auto;
        }

        .pagination {
            border-block-start: 1px solid #ddd;
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            margin-top: 20px;
            text-align: center;
            background-color: #ffffff;
        }

        .pagination a {
            display: inline-block;
            margin: 0 5px;
            padding: 5px 10px;
            background-color: #f5f5f5;
            border: 1px solid #ddd;
            text-decoration: none;
        }

        .pagination a.active {
            background-color: #ddd;
        }
    </style>
</head>
<body>
<#--<h1>日志信息</h1>-->

<div id="log-container"></div>

<div class="pagination"></div>

<script>
    // var currentPage = 1;
    // var logsPerPage = 10;
    displayLogs(1);

    function displayLogs(page) {
        var url = '/powerjob/instance/log?appId=${appId}&instanceId=${instanceId}&index=' + page; // 替换为实际的分页接口 URL
        fetch(url)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {

                var logContainer = document.getElementById('log-container');
                logContainer.innerHTML = '';
                var logEntry = document.createElement('div');
                logEntry.className = 'log-entry';
                logEntry.textContent = data.data.data;
                logContainer.appendChild(logEntry);

                displayPagination(data.data.index, data.data.totalPages);
            })
            .catch(function (error) {
                console.log('Error:', error);
            });
    }

    function displayPagination(currentPage, totalPages) {
        var paginationContainer = document.querySelector('.pagination');
        paginationContainer.innerHTML = '';

        for (var i = 1; i <= totalPages; i++) {
            var pageLink = document.createElement('a');
            pageLink.href = '#';
            pageLink.textContent = i;

            if (i === currentPage) {
                pageLink.className = 'active';
            }

            pageLink.addEventListener('click', function (event) {
                event.preventDefault();
                currentPage = parseInt(this.textContent);
                displayLogs(currentPage);
            });

            paginationContainer.appendChild(pageLink);
        }
    }

</script>
</body>
</html>