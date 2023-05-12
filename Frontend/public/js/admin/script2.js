import { baseUrl, userApiUrl } from "../shared.js";
import { isAuthenticatedWithRedirect } from "../auth/auth.js";

window.addEventListener('load', async ev => {
  await isAuthenticatedWithRedirect();

  try {
    const businesses = await fetchBusinesses();
    console.log(businesses);
    displayReportData(businesses);
  } catch (error) {
    console.error(error);
  }
});

async function fetchBusinesses() {
  try {
    const response = await fetch(`${userApiUrl}/api/admin/business`, {
      headers: {
        Authorization: `Bearer ${window.localStorage.getItem("jwt")}`
      }
    });

    if (!response.ok) {
      throw new Error("Failed to fetch businesses");
    }

    const businesses = await response.json();
    console.log(businesses);
    const reportData = [];

    for (const business of businesses) {
      const reportResponse = await fetch(`${userApiUrl}/api/user/seat/business/${business.name}`);
      const reports = await reportResponse.json();
      console.log(reports);

      let totalReports = 0;
      let totalUsage = 0;

      for (const report of reports) {
        totalReports++;
        totalUsage += report.usage;
      }

      const averageUsage = totalUsage / totalReports;

      reportData.push({
        businessName: business.name,
        totalReports: totalReports,
        totalUsage: totalUsage,
        averageUsage: averageUsage
      });
    }

    return reportData;
  } catch (error) {
    throw new Error(`Failed to fetch businesses: ${error.message}`);
  }
}

function displayReportData(reportData) {
  const reportTable = document.getElementById('report-table');
  reportTable.innerHTML = '';

  for (const report of reportData) {
    const row = reportTable.insertRow();
    row.insertCell().textContent = report.businessName;
    row.insertCell().textContent = report.totalReports;
    row.insertCell().textContent = report.totalUsage;
    row.insertCell().textContent = report.averageUsage.toFixed(2);
  }
}
