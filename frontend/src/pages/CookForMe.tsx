import { useState } from 'react'
import styled from 'styled-components'

// 스타일드 컴포넌트
const Container = styled.div`
  width: 390px;
  height: 844px;
  background-color: aliceblue;
`

const Map = styled.div`
  width: 100%;
  height: 50%;
  background-color: #b7ddff;
`

const ChefListContainer = styled.div`
  padding: 20px;
  .info {
    display: flex;
    justify-content: space-between;
    .select {
      position: relative;
      width: 120px;
      background-color: #ffffff;
      text-align: left;
      border: 1px solid #000000;
      box-sizing: border-box;
      .selected {
        border-bottom: 1px solid #000000;
        .arrow {
          position: absolute;
          top: 0;
          right: 4px;
          line-height: 24px;
        }
      }
      .option {
        width: 100%;
        height: 24px;
        line-height: 24px;
        padding: 0 4px;
        button {
          width: 100%;
          height: 100%;
          padding: 0;
        }
      }
      .sort-option {
        .option {
          &:hover {
            background-color: #ececec;
          }
        }
      }
    }
  }
`

function CookForMe() {
  const [option, setOption] = useState('거리 순')
  const optionHandler = e => {
    setOption(e.target.textContent)
  }
  return (
    <Container>
      <Map />
      <ChefListContainer>
        <div className="info">
          <h2>내 주변 요리사</h2>
          <div className="select">
            <div className="selected">
              <div className="option">{option}</div>
              <div className="arrow">v</div>
            </div>
            <ul className="sort-option">
              <li className="option">
                <button type="button" onClick={optionHandler}>
                  거리 순
                </button>
              </li>
              <li className="option">
                <button type="button" onClick={optionHandler}>
                  평점 순
                </button>
              </li>
              <li className="option">
                <button type="button" onClick={optionHandler}>
                  리뷰 많은 순
                </button>
              </li>
              <li className="option">
                <button type="button" onClick={optionHandler}>
                  레시피 많은 순
                </button>
              </li>
            </ul>
          </div>
        </div>
      </ChefListContainer>
    </Container>
  )
}

export default CookForMe
