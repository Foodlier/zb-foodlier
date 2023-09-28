import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

// 스타일드 컴포넌트
export const Container = styled.div`
  width: 100%;
`

export const Map = styled.div`
  width: 100%;
  height: 440px;
  background-color: teal;
`

export const ChefListContainer = styled.div`
  padding: 20px 20px 0 20px;
`
export const Info = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`
export const SubTitle = styled.span`
  font-weight: bold;
  font-size: 2rem;
`

export const SelectBox = styled.div`
  width: 150px;
  height: 100%;

  box-sizing: border-box;
  text-align: left;
`

export const SelectedBox = styled.div`
  position: relative;
  font-weight: bold;
  font-size: 2rem;
`

export const OptionList = styled.ul<{ $toggle: boolean }>`
  display: ${props => (props.$toggle ? 'block' : 'none')};
  width: 100%;
  position: absolute;
  top: 30px;
  left: 0;
  background-color: ${palette.white};
  border: 1px solid ${palette.divider};
  padding-left: 0;
  border-radius: 5px;
`

export const Option = styled.li`
  &:hover {
    background-color: ${palette.divider};
  }
`

export const OptionButton = styled.button`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  text-align: right;
  padding: 0 4px;
`
export const CardList = styled.ul`
  width: 100%;
  height: 500px;
  overflow: auto;
  padding-left: 0;
  &::-webkit-scrollbar {
    display: none;
  }
`

export const Card = styled.li`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 220px;
  padding: 20px;
  box-sizing: border-box;
  margin-bottom: 10px;
  border: 1px solid ${palette.divider};
  border-radius: 10px;
`
export const CardInfo = styled.div`
  display: flex;
  gap: 20px;
  .mainImg {
    width: 90px;
    height: 90px;
    background-color: ${palette.divider};
    border-radius: 10px;
  }
`

export const ChefInfo = styled.div`
  display: flex;
  flex-direction: column;
  padding-top: 10px;
  gap: 16px;
`

export const ChefTopInfo = styled.div`
  display: flex;
  align-items: center;
  height: 20px;
  .nickName {
    font-size: 20px;
    font-weight: bold;
  }
`

export const ChefBottomInfo = styled.p`
  max-width: 188px;
`
export const ElseInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`

export const RequestButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #e45141;
  color: ${palette.white};
  border-radius: 5px;
`

export const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #e45141;
  color: ${palette.white};
  border-radius: 5px;
`

export const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;
  border-top: 1px solid #d9d9d9d9;
  padding-top: 10px;
  ${breakpoints.large} {
    justify-content: end;
    padding: 10px 20px 0 0;
  }
  button {
    margin-bottom: 110px;
    ${breakpoints.large} {
      margin-bottom: 10px;
    }
  }
`
