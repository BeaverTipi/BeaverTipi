/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
    const ctx = document.querySelector('#vacancyChart').getContext('2d');

    const vacancyChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['공실', '입주'],
        datasets: [{
          data: [30, 70],  // 예시: 공실 30%, 입주 70%
          backgroundColor: ['#e74c3c', '#2ecc71']
        }]
      },
      options: {
        plugins: {
          legend: {
            position: 'bottom'
          }
        }
      }
    });
})