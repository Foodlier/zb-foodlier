import styled from 'styled-components'
import { palette } from '../../constants/Styles'

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
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`
export const CardInfo = styled.div`
  display: flex;
  gap: 1.5rem;
  .mainImg {
    width: 90px;
    height: 90px;
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
  gap: 5px;
  align-items: center;
  height: 20px;
  .nickName {
    font-size: 20px;
    font-weight: bold;
    margin-right: 5px;
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
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 5px;
`
export const RequestedButton = styled.button<{ $isActive: boolean }>`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: ${props =>
    props.$isActive ? palette.white : palette.white};
  color: ${props =>
    props.$isActive ? palette.main : palette.textDisablePlace};
  border: 1px solid
    ${props => (props.$isActive ? palette.main : palette.textDisablePlace)};
  border-radius: 5px;
  margin-left: 1rem;
`

export const FlexWrap = styled.div`
  display: flex;
  align-items: center;
`
