/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
    // 공시 비율 도넛 차트
    new Chart(document.getElementById('gongsiChartA'), {
      type: 'doughnut',
      data: {
        labels: ['공시율', '입주율'],
        datasets: [{
          data: [30, 70],
          backgroundColor: ['#e74c3c', '#2ecc71']
        }]
      },
      options: {
        plugins: { legend: { position: 'bottom' } }
      }
    });

    new Chart(document.getElementById('gongsiChartB'), {
      type: 'doughnut',
      data: {
        labels: ['공시율', '입주율'],
        datasets: [{
          data: [10, 90],
          backgroundColor: ['#e74c3c', '#2ecc71']
        }]
      },
      options: {
        plugins: { legend: { position: 'bottom' } }
      }
    });

    // 납부 금액 막대 차트
    new Chart(document.getElementById('paymentChartA'), {
      type: 'bar',
      data: {
        labels: ['1월', '2월', '3월', '4월'],
        datasets: [
          {
            label: '관리비',
            data: [5, 10, 15, 20],
            backgroundColor: '#f39c12'
          },
          {
            label: '공과금',
            data: [8, 12, 18, 22],
            backgroundColor: '#3498db'
          }
        ]
      },
      options: {
        plugins: { legend: { position: 'top' } },
        scales: {
          x: { ticks: { font: { size: 10 } } },
          y: { ticks: { font: { size: 10 } } }
        }
      }
    });

    new Chart(document.getElementById('paymentChartB'), {
      type: 'bar',
      data: {
        labels: ['1월', '2월', '3월', '4월'],
        datasets: [
          {
            label: '관리비',
            data: [10, 8, 15, 25],
            backgroundColor: '#f39c12'
          },
          {
            label: '공과금',
            data: [14, 11, 17, 24],
            backgroundColor: '#3498db'
          }
        ]
      },
      options: {
        plugins: { legend: { position: 'top' } },
        scales: {
          x: { ticks: { font: { size: 10 } } },
          y: { ticks: { font: { size: 10 } } }
        }
      }
    });
    
    })